-- ============================================================================
-- ZEXO E-COMMERCE PLATFORM: COMPLETE POSTGRESQL / SUPABASE PRODUCTION SCHEMA
-- Multi-Vendor & Multi-Source Global Marketplace (Amazon, AliExpress, Alibaba, Local)
-- ============================================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ENUMS
CREATE TYPE user_role AS ENUM ('customer', 'vendor', 'admin');
CREATE TYPE marketplace_source AS ENUM ('amazon', 'aliexpress', 'alibaba', 'local');
CREATE TYPE order_status AS ENUM ('placed', 'confirmed', 'shipped', 'out_for_delivery', 'delivered', 'cancelled');
CREATE TYPE fulfillment_type AS ENUM ('dropship', 'affiliate_redirect', 'local_dispatch', 'wholesale_b2b');
CREATE TYPE vendor_status AS ENUM ('pending_approval', 'active', 'suspended', 'rejected');

-- 1. USER PROFILES TABLE
CREATE TABLE public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    email TEXT UNIQUE NOT NULL,
    full_name TEXT,
    avatar_url TEXT,
    phone_number TEXT,
    role user_role DEFAULT 'customer' NOT NULL,
    created_at TIMESTAMPTZ DEFAULT timezone('utc'::text, now()) NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- 2. VENDORS / SELLERS TABLE
CREATE TABLE public.vendors (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE NOT NULL,
    store_name TEXT NOT NULL UNIQUE,
    store_slug TEXT NOT NULL UNIQUE,
    description TEXT,
    logo_url TEXT,
    banner_url TEXT,
    commission_rate NUMERIC(5,2) DEFAULT 8.00 NOT NULL,
    status vendor_status DEFAULT 'pending_approval' NOT NULL,
    total_sales_revenue NUMERIC(12,2) DEFAULT 0.00 NOT NULL,
    total_orders_count INTEGER DEFAULT 0 NOT NULL,
    payout_balance NUMERIC(12,2) DEFAULT 0.00 NOT NULL,
    rating NUMERIC(3,2) DEFAULT 5.00 NOT NULL,
    created_at TIMESTAMPTZ DEFAULT timezone('utc'::text, now()) NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- 3. CATEGORIES TABLE
CREATE TABLE public.categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL,
    slug TEXT NOT NULL UNIQUE,
    description TEXT,
    icon_name TEXT,
    display_order INTEGER DEFAULT 0 NOT NULL,
    created_at TIMESTAMPTZ DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- 4. PRODUCTS TABLE (Local + Amazon, AliExpress, Alibaba)
CREATE TABLE public.products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sku TEXT UNIQUE NOT NULL,
    title TEXT NOT NULL,
    slug TEXT UNIQUE NOT NULL,
    description TEXT,
    source marketplace_source DEFAULT 'local' NOT NULL,
    vendor_id UUID REFERENCES public.vendors(id) ON DELETE SET NULL,
    category_id UUID REFERENCES public.categories(id) ON DELETE SET NULL,
    price NUMERIC(10,2) NOT NULL,
    original_price NUMERIC(10,2),
    cost_price NUMERIC(10,2),
    stock_quantity INTEGER DEFAULT 0 NOT NULL,
    moq INTEGER DEFAULT 1 NOT NULL,
    rating NUMERIC(3,2) DEFAULT 0.00 NOT NULL,
    reviews_count INTEGER DEFAULT 0 NOT NULL,
    is_flash_sale BOOLEAN DEFAULT FALSE NOT NULL,
    flash_discount_percent INTEGER DEFAULT 0,
    is_featured BOOLEAN DEFAULT FALSE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE NOT NULL,
    external_platform_id TEXT,
    external_affiliate_url TEXT,
    external_supplier_name TEXT,
    estimated_delivery_days TEXT DEFAULT '7-12 days tracked',
    created_at TIMESTAMPTZ DEFAULT timezone('utc'::text, now()) NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- 5. EXTERNAL API CONFIGURATIONS TABLE (Amazon PA-API, AliExpress, Alibaba)
CREATE TABLE public.external_api_configs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    platform_key TEXT UNIQUE NOT NULL,
    platform_name TEXT NOT NULL,
    api_key TEXT NOT NULL,
    api_secret TEXT NOT NULL,
    tracking_or_associate_id TEXT NOT NULL,
    is_connected BOOLEAN DEFAULT TRUE NOT NULL,
    auto_sync_stock BOOLEAN DEFAULT TRUE NOT NULL,
    profit_margin_percent NUMERIC(5,2) DEFAULT 18.00 NOT NULL,
    last_sync_status TEXT DEFAULT 'Ready',
    last_synced_at TIMESTAMPTZ,
    total_synced_count INTEGER DEFAULT 0 NOT NULL,
    created_at TIMESTAMPTZ DEFAULT timezone('utc'::text, now()) NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- 6. ORDERS TABLE
CREATE TABLE public.orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_code TEXT UNIQUE NOT NULL,
    customer_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    total_amount NUMERIC(10,2) NOT NULL,
    discount_amount NUMERIC(10,2) DEFAULT 0.00 NOT NULL,
    shipping_fee NUMERIC(10,2) DEFAULT 0.00 NOT NULL,
    promo_code_applied TEXT,
    status order_status DEFAULT 'placed' NOT NULL,
    current_step_index INTEGER DEFAULT 0 NOT NULL,
    fulfillment_type fulfillment_type DEFAULT 'dropship' NOT NULL,
    shipping_address JSONB NOT NULL,
    payment_method TEXT NOT NULL,
    payment_status TEXT DEFAULT 'paid' NOT NULL,
    primary_source marketplace_source DEFAULT 'local' NOT NULL,
    global_tracking_number TEXT,
    created_at TIMESTAMPTZ DEFAULT timezone('utc'::text, now()) NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- 7. CART & WISHLIST TABLES
CREATE TABLE public.cart_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE NOT NULL,
    product_id UUID REFERENCES public.products(id) ON DELETE CASCADE NOT NULL,
    quantity INTEGER DEFAULT 1 NOT NULL CHECK (quantity > 0),
    created_at TIMESTAMPTZ DEFAULT timezone('utc'::text, now()) NOT NULL
);

CREATE TABLE public.wishlists (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE NOT NULL,
    product_id UUID REFERENCES public.products(id) ON DELETE CASCADE NOT NULL,
    created_at TIMESTAMPTZ DEFAULT timezone('utc'::text, now()) NOT NULL,
    UNIQUE(user_id, product_id)
);
