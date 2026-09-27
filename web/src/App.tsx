import React, { useState, useEffect, useMemo } from 'react';
import { 
  ShoppingBag, Search, Sun, Moon, ShieldCheck, Truck, RefreshCw, 
  ExternalLink, Star, Heart, Plus, Minus, Trash2, CheckCircle2, 
  ArrowRight, Store, Settings, Package, SlidersHorizontal, 
  Globe, Zap, Filter, X, ChevronRight, Copy, Check
} from 'lucide-react';
import { Product, CartItem, Order, MarketplaceSource, ShippingAddress } from './types';
import { INITIAL_PRODUCTS } from './data/mockData';
import { GlobalMarketplaceSyncService } from '../../services/globalMarketplaceSync';

export default function App() {
  // Theme & User Role Mode
  const [darkMode, setDarkMode] = useState<boolean>(true);
  const [activeRole, setActiveRole] = useState<'customer' | 'vendor' | 'admin'>('customer');
  
  // Products, Search & Filters
  const [products, setProducts] = useState<Product[]>(INITIAL_PRODUCTS);
  const [selectedSource, setSelectedSource] = useState<MarketplaceSource | 'all'>('all');
  const [selectedCategory, setSelectedCategory] = useState<string>('All');
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [priceSort, setPriceSort] = useState<'none' | 'low-to-high' | 'high-to-low'>('none');

  // Flash Sale Countdown Timer
  const [flashTimeLeft, setFlashTimeLeft] = useState<{ hours: number; minutes: number; seconds: number }>({
    hours: 8,
    minutes: 42,
    seconds: 15
  });

  // Modals & Panels
  const [activeProductModal, setActiveProductModal] = useState<Product | null>(null);
  const [isCartOpen, setIsCartOpen] = useState<boolean>(false);
  const [isCheckoutOpen, setIsCheckoutOpen] = useState<boolean>(false);
  const [cartItems, setCartItems] = useState<CartItem[]>([
    { product: INITIAL_PRODUCTS[0], quantity: 1, selectedColor: 'Midnight Black', selectedSize: 'Standard Over-Ear' },
    { product: INITIAL_PRODUCTS[1], quantity: 1, selectedColor: 'Neon Cyan', selectedSize: 'Red Switches' }
  ]);
  const [wishlist, setWishlist] = useState<string[]>(['prod-4']);
  const [copiedTracking, setCopiedTracking] = useState<boolean>(false);

  // Active Orders & Order Tracking
  const [activeOrder, setActiveOrder] = useState<Order | null>(null);
  const [showOrderTracker, setShowOrderTracker] = useState<boolean>(false);

  // Admin Sync Status
  const [isSyncingGlobalApis, setIsSyncingGlobalApis] = useState<boolean>(false);
  const [syncNotice, setSyncNotice] = useState<string | null>(null);

  // Checkout Multi-Step Form State
  const [checkoutStep, setCheckoutStep] = useState<number>(1);
  const [shippingAddress, setShippingAddress] = useState<ShippingAddress>({
    fullName: 'Ali Hassan',
    email: 'ali.hassan@example.com',
    phone: '+92 300 1234567',
    street: '74 Gulberg III, Main Boulevard',
    city: 'Lahore',
    state: 'Punjab',
    postalCode: '54000',
    country: 'Pakistan'
  });
  const [deliveryMethod, setDeliveryMethod] = useState<'express' | 'standard'>('express');
  const [paymentMethod, setPaymentMethod] = useState<'card' | 'upi' | 'cod'>('card');
  const [couponCode, setCouponCode] = useState<string>('ZEXO10');
  const [couponDiscountPercent, setCouponDiscountPercent] = useState<number>(10);

  // Vendor Add Product State
  const [vendorNewTitle, setVendorNewTitle] = useState('');
  const [vendorNewPrice, setVendorNewPrice] = useState('');
  const [vendorNewCategory, setVendorNewCategory] = useState('Electronics');
  const [vendorNewStock, setVendorNewStock] = useState('25');

  // Flash Sale Timer Effect
  useEffect(() => {
    const timer = setInterval(() => {
      setFlashTimeLeft(prev => {
        if (prev.seconds > 0) return { ...prev, seconds: prev.seconds - 1 };
        if (prev.minutes > 0) return { ...prev, minutes: prev.minutes - 1, seconds: 59 };
        if (prev.hours > 0) return { hours: prev.hours - 1, minutes: 59, seconds: 59 };
        return { hours: 12, minutes: 0, seconds: 0 };
      });
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  // Filtered & Sorted Products
  const filteredProducts = useMemo(() => {
    return products
      .filter(p => {
        const matchesSource = selectedSource === 'all' || p.source === selectedSource;
        const matchesCategory = selectedCategory === 'All' || p.category === selectedCategory;
        const matchesQuery = p.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
                             p.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
                             p.sellerName.toLowerCase().includes(searchQuery.toLowerCase());
        return matchesSource && matchesCategory && matchesQuery;
      })
      .sort((a, b) => {
        if (priceSort === 'low-to-high') return a.price - b.price;
        if (priceSort === 'high-to-low') return b.price - a.price;
        return 0;
      });
  }, [products, selectedSource, selectedCategory, searchQuery, priceSort]);

  // Cart Calculations
  const cartSubtotal = useMemo(() => {
    return cartItems.reduce((acc, item) => acc + item.product.price * item.quantity, 0);
  }, [cartItems]);

  const discountAmount = useMemo(() => {
    return (cartSubtotal * couponDiscountPercent) / 100;
  }, [cartSubtotal, couponDiscountPercent]);

  const shippingCost = cartSubtotal > 150 ? 0 : 15.00;
  const orderTotal = Math.max(0, cartSubtotal - discountAmount + shippingCost);

  // Cart Actions
  const addToCart = (product: Product, color?: string, size?: string) => {
    setCartItems(prev => {
      const existing = prev.find(item => item.product.id === product.id);
      if (existing) {
        return prev.map(item =>
          item.product.id === product.id ? { ...item, quantity: item.quantity + 1 } : item
        );
      }
      return [...prev, {
        product,
        quantity: 1,
        selectedColor: color || product.variants?.colors[0],
        selectedSize: size || product.variants?.sizes[0]
      }];
    });
    setIsCartOpen(true);
  };

  const updateCartQty = (productId: string, delta: number) => {
    setCartItems(prev => prev.map(item => {
      if (item.product.id === productId) {
        const newQty = item.quantity + delta;
        return newQty > 0 ? { ...item, quantity: newQty } : item;
      }
      return item;
    }));
  };

  const removeFromCart = (productId: string) => {
    setCartItems(prev => prev.filter(item => item.product.id !== productId));
  };

  const toggleWishlist = (productId: string) => {
    setWishlist(prev => 
      prev.includes(productId) ? prev.filter(id => id !== productId) : [...prev, productId]
    );
  };

  // Place Order Action
  const handlePlaceOrder = async () => {
    const orderCode = `ZXO-2026-${Math.floor(1000 + Math.random() * 9000)}`;
    const trackingCode = `AE-US-${Math.floor(100000000 + Math.random() * 900000000)}`;
    const primarySource = cartItems[0]?.product.source || 'local';

    const newOrder: Order = {
      id: `ord-${Date.now()}`,
      orderCode,
      date: new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }),
      items: [...cartItems],
      subtotal: cartSubtotal,
      discount: discountAmount,
      shipping: shippingCost,
      total: orderTotal,
      paymentMethod: paymentMethod === 'card' ? 'Visa •••• 4242' : paymentMethod === 'upi' ? 'UPI / Wallet' : 'Cash on Delivery',
      shippingAddress: { ...shippingAddress },
      status: 'Order Placed',
      stepIndex: 1,
      trackingNumber: trackingCode,
      carrier: primarySource === 'amazon' ? 'Amazon Logistics' : primarySource === 'aliexpress' ? 'AliExpress Standard' : 'DHL Express',
      primarySource
    };

    if (primarySource !== 'local') {
      await GlobalMarketplaceSyncService.forwardOrderToSupplier({
        orderCode,
        supplierSource: primarySource,
        supplierProductId: cartItems[0]?.product.sku || 'EXT-101',
        quantity: cartItems[0]?.quantity || 1,
        shippingAddress: { ...shippingAddress }
      });
    }

    setActiveOrder(newOrder);
    setCartItems([]);
    setIsCheckoutOpen(false);
    setShowOrderTracker(true);
  };

  // Admin Trigger Global Marketplace Sync
  const handleAdminSync = async () => {
    setIsSyncingGlobalApis(true);
    setSyncNotice(null);
    try {
      await GlobalMarketplaceSyncService.syncAmazonProducts(
        ['B09XS7JWHH'],
        'AKIAIOSFODNN7EXAMPLE',
        'wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY',
        'zexo-global-20'
      );
      setSyncNotice(`Successfully synced live products & pricing from Amazon PA-API, AliExpress & Alibaba.`);
    } catch {
      setSyncNotice('Sync failed. Please check API keys.');
    } finally {
      setIsSyncingGlobalApis(false);
    }
  };

  // Vendor Add Product
  const handleVendorAddProduct = (e: React.FormEvent) => {
    e.preventDefault();
    if (!vendorNewTitle || !vendorNewPrice) return;
    const newProd: Product = {
      id: `prod-${Date.now()}`,
      sku: `LOC-${Math.floor(1000 + Math.random() * 9000)}`,
      title: vendorNewTitle,
      description: 'Locally handcrafted product directly from verified vendor.',
      category: vendorNewCategory,
      price: parseFloat(vendorNewPrice),
      originalPrice: parseFloat(vendorNewPrice) * 1.25,
      rating: 5.0,
      reviewsCount: 1,
      source: 'local',
      sellerName: 'My Vendor Boutique',
      stockQuantity: parseInt(vendorNewStock) || 20,
      images: ['https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=800'],
      isFlashSale: false,
      estimatedDelivery: '1-3 Days Express Courier'
    };
    setProducts(prev => [newProd, ...prev]);
    setVendorNewTitle('');
    setVendorNewPrice('');
    alert('Product successfully published to Zexo Global Marketplace!');
  };

  return (
    <div className={darkMode ? 'dark bg-slate-950 text-slate-100 min-h-screen' : 'bg-slate-50 text-slate-900 min-h-screen'}>
      {/* 1. TOP TICKER & ANNOUNCEMENT BAR */}
      <div className="bg-gradient-to-r from-blue-700 via-indigo-600 to-amber-500 text-white text-xs font-semibold px-4 py-2 flex flex-wrap justify-between items-center gap-2">
        <div className="flex items-center gap-2">
          <Globe className="w-4 h-4 animate-spin text-amber-300" style={{ animationDuration: '8s' }} />
          <span>GLOBAL STORE INTEGRATION LIVE: Amazon, AliExpress & Alibaba verified items direct to your door!</span>
        </div>
        <div className="flex items-center gap-4">
          <div className="flex items-center gap-1.5 bg-black/25 px-2 py-0.5 rounded-full font-mono">
            <Zap className="w-3.5 h-3.5 text-amber-300 fill-amber-300" />
            <span>Flash Deals End in: {String(flashTimeLeft.hours).padStart(2, '0')}:{String(flashTimeLeft.minutes).padStart(2, '0')}:{String(flashTimeLeft.seconds).padStart(2, '0')}</span>
          </div>
          <div className="flex items-center gap-1 border-l border-white/30 pl-3">
            <button 
              onClick={() => setActiveRole('customer')} 
              className={`px-2 py-0.5 rounded text-[11px] ${activeRole === 'customer' ? 'bg-white text-blue-900 font-bold' : 'hover:bg-white/20'}`}>
              Customer
            </button>
            <button 
              onClick={() => setActiveRole('vendor')} 
              className={`px-2 py-0.5 rounded text-[11px] ${activeRole === 'vendor' ? 'bg-white text-blue-900 font-bold' : 'hover:bg-white/20'}`}>
              Vendor Portal
            </button>
            <button 
              onClick={() => setActiveRole('admin')} 
              className={`px-2 py-0.5 rounded text-[11px] ${activeRole === 'admin' ? 'bg-white text-blue-900 font-bold' : 'hover:bg-white/20'}`}>
              Admin Control
            </button>
          </div>
        </div>
      </div>

      {/* 2. MAIN HEADER & NAVIGATION */}
      <header className="sticky top-0 z-40 backdrop-blur-md bg-white/90 dark:bg-slate-900/90 border-b border-slate-200 dark:border-slate-800 transition-colors">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-3.5 flex items-center justify-between gap-4">
          <div className="flex items-center gap-2 cursor-pointer" onClick={() => { setShowOrderTracker(false); setActiveRole('customer'); }}>
            <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-blue-700 via-indigo-600 to-amber-400 flex items-center justify-center text-white shadow-md shadow-blue-500/20">
              <ShoppingBag className="w-5 h-5" />
            </div>
            <div>
              <span className="text-2xl font-black tracking-tight text-blue-600 dark:text-blue-400">ZEXO</span>
              <span className="text-xs uppercase tracking-widest block font-bold text-amber-500">GLOBAL STORE</span>
            </div>
          </div>

          <div className="flex-1 max-w-2xl relative hidden md:block">
            <div className="relative flex items-center">
              <input
                type="text"
                placeholder="Search products from Amazon, AliExpress, Alibaba & Local Stores..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-11 pr-24 py-2.5 rounded-full border border-slate-300 dark:border-slate-700 bg-slate-100 dark:bg-slate-800/80 focus:outline-none focus:ring-2 focus:ring-blue-600 text-sm placeholder:text-slate-400 dark:text-white"
              />
              <Search className="w-4 h-4 absolute left-4 text-slate-400" />
              <button 
                onClick={() => setSearchQuery('')}
                className="absolute right-2 px-3 py-1 bg-blue-600 hover:bg-blue-700 text-white rounded-full text-xs font-semibold transition-colors">
                Search
              </button>
            </div>
          </div>

          <div className="flex items-center gap-3">
            <button
              onClick={() => setDarkMode(!darkMode)}
              className="p-2 rounded-xl border border-slate-200 dark:border-slate-800 text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
              title="Toggle Dark/Light Mode">
              {darkMode ? <Sun className="w-5 h-5 text-amber-400" /> : <Moon className="w-5 h-5 text-blue-600" />}
            </button>

            {activeOrder && (
              <button
                onClick={() => setShowOrderTracker(true)}
                className="hidden sm:flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-amber-500/10 border border-amber-500/30 text-amber-600 dark:text-amber-400 text-xs font-bold hover:bg-amber-500/20 transition-all">
                <Truck className="w-4 h-4" />
                <span>Track Order</span>
              </button>
            )}

            <button
              onClick={() => setIsCartOpen(true)}
              className="relative p-2.5 rounded-xl bg-blue-600 text-white hover:bg-blue-700 transition-all shadow-md shadow-blue-600/30 flex items-center gap-2">
              <ShoppingBag className="w-5 h-5" />
              <span className="font-bold text-sm hidden sm:inline">${orderTotal.toFixed(2)}</span>
              {cartItems.length > 0 && (
                <span className="absolute -top-1.5 -right-1.5 w-5 h-5 bg-amber-400 text-slate-950 font-black rounded-full text-[11px] flex items-center justify-center ring-2 ring-white dark:ring-slate-900">
                  {cartItems.reduce((acc, i) => acc + i.quantity, 0)}
                </span>
              )}
            </button>
          </div>
        </div>
      </header>

      {/* 3. CONDITIONAL VIEWS: VENDOR, ADMIN, OR STOREFRONT */}
      {activeRole === 'vendor' ? (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="bg-white dark:bg-slate-900 rounded-3xl p-6 sm:p-8 border border-slate-200 dark:border-slate-800 shadow-xl mb-8">
            <div className="flex flex-wrap items-center justify-between gap-4 pb-6 border-b border-slate-200 dark:border-slate-800">
              <div className="flex items-center gap-4">
                <div className="w-14 h-14 rounded-2xl bg-indigo-600/10 text-indigo-600 dark:text-indigo-400 flex items-center justify-center font-black text-2xl border border-indigo-600/20">
                  <Store className="w-7 h-7" />
                </div>
                <div>
                  <h1 className="text-2xl font-black">Vendor Management Dashboard</h1>
                  <p className="text-sm text-slate-500">Manage your local inventory, track sales revenue, and fulfill orders.</p>
                </div>
              </div>
              <div className="flex items-center gap-3">
                <div className="px-4 py-2 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 text-emerald-600 dark:text-emerald-400 font-bold text-sm">
                  Active Status: Approved Seller
                </div>
                <div className="px-4 py-2 rounded-2xl bg-blue-500/10 border border-blue-500/20 text-blue-600 dark:text-blue-400 font-bold text-sm">
                  Commission: 8.0%
                </div>
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-4 gap-4 mt-6">
              <div className="p-5 rounded-2xl bg-slate-50 dark:bg-slate-800/50 border border-slate-200 dark:border-slate-700/60">
                <span className="text-xs text-slate-400 font-semibold uppercase">Total Revenue</span>
                <p className="text-2xl font-black mt-1 text-slate-900 dark:text-white">$14,892.50</p>
                <span className="text-[11px] text-emerald-500 font-bold mt-1 inline-block">↑ +18.4% from last month</span>
              </div>
              <div className="p-5 rounded-2xl bg-slate-50 dark:bg-slate-800/50 border border-slate-200 dark:border-slate-700/60">
                <span className="text-xs text-slate-400 font-semibold uppercase">Fulfilled Orders</span>
                <p className="text-2xl font-black mt-1 text-slate-900 dark:text-white">184 orders</p>
                <span className="text-[11px] text-blue-500 font-bold mt-1 inline-block">99.2% on-time delivery</span>
              </div>
              <div className="p-5 rounded-2xl bg-slate-50 dark:bg-slate-800/50 border border-slate-200 dark:border-slate-700/60">
                <span className="text-xs text-slate-400 font-semibold uppercase">Available Payout</span>
                <p className="text-2xl font-black mt-1 text-amber-500">$2,410.80</p>
                <button className="text-[11px] bg-amber-500 hover:bg-amber-600 text-slate-950 font-bold px-2 py-0.5 rounded mt-1">Withdraw Payout</button>
              </div>
              <div className="p-5 rounded-2xl bg-slate-50 dark:bg-slate-800/50 border border-slate-200 dark:border-slate-700/60">
                <span className="text-xs text-slate-400 font-semibold uppercase">Store Rating</span>
                <p className="text-2xl font-black mt-1 text-amber-400">4.9 / 5.0</p>
                <span className="text-[11px] text-slate-400 mt-1 inline-block">Based on 142 reviews</span>
              </div>
            </div>

            <div className="mt-8 pt-8 border-t border-slate-200 dark:border-slate-800">
              <h2 className="text-lg font-black mb-4 flex items-center gap-2">
                <Plus className="w-5 h-5 text-blue-500" />
                Upload New Product to Zexo Marketplace
              </h2>
              <form onSubmit={handleVendorAddProduct} className="grid grid-cols-1 sm:grid-cols-4 gap-4">
                <div className="sm:col-span-2">
                  <label className="text-xs font-bold text-slate-400 block mb-1">Product Title</label>
                  <input
                    type="text"
                    required
                    placeholder="e.g. Ergonomic Walnut Desk Shelf"
                    value={vendorNewTitle}
                    onChange={(e) => setVendorNewTitle(e.target.value)}
                    className="w-full px-4 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-slate-100 dark:bg-slate-800 text-sm focus:ring-2 focus:ring-blue-600 focus:outline-none"
                  />
                </div>
                <div>
                  <label className="text-xs font-bold text-slate-400 block mb-1">Selling Price ($)</label>
                  <input
                    type="number"
                    step="0.01"
                    required
                    placeholder="99.00"
                    value={vendorNewPrice}
                    onChange={(e) => setVendorNewPrice(e.target.value)}
                    className="w-full px-4 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-slate-100 dark:bg-slate-800 text-sm focus:ring-2 focus:ring-blue-600 focus:outline-none"
                  />
                </div>
                <div>
                  <label className="text-xs font-bold text-slate-400 block mb-1">Stock Quantity</label>
                  <input
                    type="number"
                    required
                    placeholder="50"
                    value={vendorNewStock}
                    onChange={(e) => setVendorNewStock(e.target.value)}
                    className="w-full px-4 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-slate-100 dark:bg-slate-800 text-sm focus:ring-2 focus:ring-blue-600 focus:outline-none"
                  />
                </div>
                <div className="sm:col-span-3">
                  <label className="text-xs font-bold text-slate-400 block mb-1">Category</label>
                  <select 
                    value={vendorNewCategory}
                    onChange={(e) => setVendorNewCategory(e.target.value)}
                    className="w-full px-4 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-slate-100 dark:bg-slate-800 text-sm focus:ring-2 focus:ring-blue-600 focus:outline-none">
                    <option value="Electronics">Electronics</option>
                    <option value="Gadgets">Gadgets</option>
                    <option value="Fashion">Fashion</option>
                    <option value="Home & Living">Home & Living</option>
                  </select>
                </div>
                <div className="flex items-end">
                  <button
                    type="submit"
                    className="w-full py-2.5 px-4 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl text-sm transition-all shadow-md shadow-blue-600/30">
                    Publish Product
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      ) : activeRole === 'admin' ? (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="bg-white dark:bg-slate-900 rounded-3xl p-6 sm:p-8 border border-slate-200 dark:border-slate-800 shadow-xl mb-8">
            <div className="flex flex-wrap items-center justify-between gap-4 pb-6 border-b border-slate-200 dark:border-slate-800">
              <div className="flex items-center gap-4">
                <div className="w-14 h-14 rounded-2xl bg-amber-500/10 text-amber-500 flex items-center justify-center font-black text-2xl border border-amber-500/20">
                  <Settings className="w-7 h-7" />
                </div>
                <div>
                  <h1 className="text-2xl font-black">Admin Superuser Control Panel</h1>
                  <p className="text-sm text-slate-500">Oversee global marketplace integrations, supplier sync & platform commissions.</p>
                </div>
              </div>
              <button
                onClick={handleAdminSync}
                disabled={isSyncingGlobalApis}
                className="flex items-center gap-2 px-5 py-2.5 rounded-2xl bg-blue-600 hover:bg-blue-700 text-white font-bold text-sm shadow-lg shadow-blue-600/30 transition-all disabled:opacity-50">
                <RefreshCw className={`w-4 h-4 ${isSyncingGlobalApis ? 'animate-spin' : ''}`} />
                <span>{isSyncingGlobalApis ? 'Syncing External APIs...' : 'Trigger Global API Sync'}</span>
              </button>
            </div>

            {syncNotice && (
              <div className="mt-4 p-4 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 text-emerald-500 text-sm font-semibold flex items-center gap-2">
                <CheckCircle2 className="w-5 h-5 flex-shrink-0" />
                <span>{syncNotice}</span>
              </div>
            )}

            <h3 className="text-base font-black mt-8 mb-4">External Marketplace API Connections</h3>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
              <div className="p-6 rounded-2xl border border-slate-200 dark:border-slate-800 bg-slate-50 dark:bg-slate-800/40">
                <div className="flex justify-between items-center mb-3">
                  <span className="font-black text-lg text-amber-500">Amazon PA-API v5.0</span>
                  <span className="text-[11px] px-2 py-0.5 rounded-full bg-emerald-500/10 text-emerald-500 font-bold border border-emerald-500/20">Connected</span>
                </div>
                <p className="text-xs text-slate-400 mb-4">Syncs ASIN products, live prime discounts, reviews & affiliate tracking links.</p>
                <div className="text-xs space-y-1 text-slate-500 font-mono">
                  <div>Tag: zexo-global-20</div>
                  <div>Synced Items: 1,420 Active</div>
                  <div>Profit Margin: +15% Fixed</div>
                </div>
              </div>

              <div className="p-6 rounded-2xl border border-slate-200 dark:border-slate-800 bg-slate-50 dark:bg-slate-800/40">
                <div className="flex justify-between items-center mb-3">
                  <span className="font-black text-lg text-red-500">AliExpress Open API</span>
                  <span className="text-[11px] px-2 py-0.5 rounded-full bg-emerald-500/10 text-emerald-500 font-bold border border-emerald-500/20">Connected</span>
                </div>
                <p className="text-xs text-slate-400 mb-4">Automated dropshipping bridge. Relays orders straight to overseas fulfillment centers.</p>
                <div className="text-xs space-y-1 text-slate-500 font-mono">
                  <div>Endpoint: api-sg.aliexpress.com</div>
                  <div>Relayed Orders: 412 Placed</div>
                  <div>Avg Dispatch: 18 Hours</div>
                </div>
              </div>

              <div className="p-6 rounded-2xl border border-slate-200 dark:border-slate-800 bg-slate-50 dark:bg-slate-800/40">
                <div className="flex justify-between items-center mb-3">
                  <span className="font-black text-lg text-orange-500">Alibaba B2B Wholesale</span>
                  <span className="text-[11px] px-2 py-0.5 rounded-full bg-emerald-500/10 text-emerald-500 font-bold border border-emerald-500/20">Connected</span>
                </div>
                <p className="text-xs text-slate-400 mb-4">Factory-direct bulk sourcing with verified MOQs and freight management.</p>
                <div className="text-xs space-y-1 text-slate-500 font-mono">
                  <div>Trade Assurance: Enabled</div>
                  <div>MOQ Enforcement: Strict</div>
                  <div>Customs Clearance: Automated</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      ) : showOrderTracker && activeOrder ? (
        <div className="max-w-4xl mx-auto px-4 py-10">
          <button 
            onClick={() => setShowOrderTracker(false)}
            className="flex items-center gap-1.5 text-sm font-bold text-blue-600 dark:text-blue-400 mb-6 hover:underline">
            ← Return to Storefront
          </button>
          
          <div className="bg-white dark:bg-slate-900 rounded-3xl p-6 sm:p-8 border border-slate-200 dark:border-slate-800 shadow-xl">
            <div className="flex flex-wrap items-center justify-between gap-4 pb-6 border-b border-slate-200 dark:border-slate-800">
              <div>
                <span className="text-xs font-bold uppercase tracking-wider text-amber-500">Active Order Status</span>
                <h1 className="text-2xl font-black text-slate-900 dark:text-white mt-1">Order #{activeOrder.orderCode}</h1>
                <p className="text-xs text-slate-400 mt-1">Placed on {activeOrder.date} • Paid via {activeOrder.paymentMethod}</p>
              </div>

              <div className="flex items-center gap-2">
                <div className="bg-slate-100 dark:bg-slate-800 px-3 py-1.5 rounded-xl border border-slate-200 dark:border-slate-700 text-xs font-mono">
                  Tracking: {activeOrder.trackingNumber}
                </div>
                <button 
                  onClick={() => {
                    navigator.clipboard.writeText(activeOrder.trackingNumber);
                    setCopiedTracking(true);
                    setTimeout(() => setCopiedTracking(false), 2000);
                  }}
                  className="p-2 rounded-xl bg-blue-600 hover:bg-blue-700 text-white text-xs transition-colors">
                  {copiedTracking ? <Check className="w-4 h-4" /> : <Copy className="w-4 h-4" />}
                </button>
              </div>
            </div>

            <div className="my-8">
              <h2 className="text-sm font-bold text-slate-400 uppercase tracking-wider mb-6">Shipment Milestones</h2>
              <div className="relative flex justify-between items-center">
                <div className="absolute top-1/2 left-0 right-0 h-1 bg-slate-200 dark:bg-slate-800 -translate-y-1/2 z-0" />
                <div 
                  className="absolute top-1/2 left-0 h-1 bg-gradient-to-r from-blue-600 to-emerald-500 -translate-y-1/2 z-0 transition-all duration-700" 
                  style={{ width: `${((activeOrder.stepIndex) / 4) * 100}%` }}
                />

                {[
                  { step: 0, label: 'Order Placed', desc: 'Verified & Paid' },
                  { step: 1, label: 'Supplier Confirmed', desc: activeOrder.primarySource === 'local' ? 'Local Vendor' : `${activeOrder.primarySource.toUpperCase()} Supplier` },
                  { step: 2, label: 'Air/Express Freight', desc: 'In Transit' },
                  { step: 3, label: 'Out for Delivery', desc: 'Local Courier' },
                  { step: 4, label: 'Delivered', desc: 'Destination' },
                ].map((s) => {
                  const isDone = s.step <= activeOrder.stepIndex;
                  return (
                    <div key={s.step} className="relative z-10 flex flex-col items-center text-center">
                      <div className={`w-8 h-8 rounded-full flex items-center justify-center font-bold text-xs shadow-md transition-all ${
                        isDone ? 'bg-emerald-500 text-white ring-4 ring-emerald-500/20' : 'bg-slate-300 dark:bg-slate-700 text-slate-500'
                      }`}>
                        {isDone ? <Check className="w-4 h-4" /> : s.step + 1}
                      </div>
                      <span className="text-xs font-bold mt-2 text-slate-800 dark:text-slate-200">{s.label}</span>
                      <span className="text-[10px] text-slate-400 hidden sm:block">{s.desc}</span>
                    </div>
                  );
                })}
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 pt-6 border-t border-slate-200 dark:border-slate-800 text-sm">
              <div>
                <h3 className="font-bold text-slate-400 text-xs uppercase mb-2">Delivery Destination</h3>
                <p className="font-bold">{activeOrder.shippingAddress.fullName}</p>
                <p className="text-slate-400 text-xs mt-1">{activeOrder.shippingAddress.street}, {activeOrder.shippingAddress.city}</p>
                <p className="text-slate-400 text-xs">{activeOrder.shippingAddress.country} ({activeOrder.shippingAddress.phone})</p>
              </div>
              <div>
                <h3 className="font-bold text-slate-400 text-xs uppercase mb-2">Order Items Summary</h3>
                <div className="space-y-2">
                  {activeOrder.items.map((i, idx) => (
                    <div key={idx} className="flex justify-between text-xs">
                      <span>{i.product.title.slice(0, 35)}... (x{i.quantity})</span>
                      <span className="font-bold">${(i.product.price * i.quantity).toFixed(2)}</span>
                    </div>
                  ))}
                  <div className="flex justify-between font-black text-sm pt-2 border-t border-slate-200 dark:border-slate-800">
                    <span>Total Paid</span>
                    <span className="text-blue-500">${activeOrder.total.toFixed(2)}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      ) : (
        <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          {/* HERO BANNER */}
          <div className="relative rounded-3xl overflow-hidden bg-gradient-to-r from-blue-900 via-indigo-900 to-slate-900 p-8 sm:p-12 mb-10 text-white shadow-2xl border border-white/10">
            <div className="relative z-10 max-w-2xl">
              <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-amber-400/20 text-amber-300 border border-amber-400/30 text-xs font-bold uppercase tracking-wider mb-4">
                <Globe className="w-3.5 h-3.5" />
                Cross-Border Global Fulfillment
              </div>
              <h1 className="text-3xl sm:text-5xl font-black tracking-tight leading-tight">
                One Cart for <span className="text-transparent bg-clip-text bg-gradient-to-r from-amber-400 to-orange-400">Amazon, AliExpress</span> & Local Boutiques
              </h1>
              <p className="text-slate-300 text-sm sm:text-base mt-4 font-normal leading-relaxed">
                Direct API integration with global manufacturers and top-rated local artisans. Guaranteed tracked shipping, customs clearance handled, and 100% buyer protection.
              </p>
              
              <div className="flex flex-wrap items-center gap-4 mt-8">
                <button 
                  onClick={() => setSelectedSource('all')}
                  className="px-6 py-3 rounded-2xl bg-amber-400 hover:bg-amber-300 text-slate-950 font-black text-sm transition-all shadow-lg shadow-amber-400/20 flex items-center gap-2">
                  <span>Explore All Products</span>
                  <ArrowRight className="w-4 h-4" />
                </button>
                <div className="flex items-center gap-2 text-xs text-slate-300">
                  <ShieldCheck className="w-4 h-4 text-emerald-400" />
                  <span>30-Day Money-Back Guarantee</span>
                </div>
              </div>
            </div>
            <div className="absolute -right-20 -bottom-20 w-96 h-96 bg-blue-500/20 rounded-full blur-3xl pointer-events-none" />
          </div>

          {/* SOURCE SELECTOR */}
          <div className="flex flex-wrap items-center justify-between gap-4 mb-6">
            <div className="flex flex-wrap items-center gap-2">
              <span className="text-xs font-bold uppercase tracking-wider text-slate-400 mr-2 flex items-center gap-1">
                <Filter className="w-3.5 h-3.5" /> Source:
              </span>
              {[
                { key: 'all', label: 'All Sources' },
                { key: 'amazon', label: 'Amazon Direct' },
                { key: 'aliexpress', label: 'AliExpress Dropship' },
                { key: 'alibaba', label: 'Alibaba B2B' },
                { key: 'local', label: 'Verified Local' }
              ].map((src) => (
                <button
                  key={src.key}
                  onClick={() => setSelectedSource(src.key as any)}
                  className={`px-4 py-2 rounded-2xl text-xs font-bold transition-all border ${
                    selectedSource === src.key
                      ? 'bg-blue-600 text-white border-blue-600 shadow-md shadow-blue-600/30'
                      : 'bg-white dark:bg-slate-900 border-slate-200 dark:border-slate-800 text-slate-600 dark:text-slate-400 hover:border-slate-300'
                  }`}>
                  {src.label}
                </button>
              ))}
            </div>

            <div className="flex items-center gap-2">
              <span className="text-xs text-slate-400 font-semibold">Sort by Price:</span>
              <select
                value={priceSort}
                onChange={(e) => setPriceSort(e.target.value as any)}
                className="px-3 py-1.5 rounded-xl border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 text-xs font-bold focus:outline-none">
                <option value="none">Default</option>
                <option value="low-to-high">Lowest to Highest</option>
                <option value="high-to-low">Highest to Lowest</option>
              </select>
            </div>
          </div>

          {/* CATEGORY TABS */}
          <div className="flex items-center gap-2 overflow-x-auto pb-4 mb-8 custom-scrollbar">
            {['All', 'Electronics', 'Gadgets', 'Fashion', 'Home & Living'].map((cat) => (
              <button
                key={cat}
                onClick={() => setSelectedCategory(cat)}
                className={`px-4 py-2 rounded-xl text-xs font-semibold whitespace-nowrap transition-colors ${
                  selectedCategory === cat
                    ? 'bg-slate-900 dark:bg-white text-white dark:text-slate-900'
                    : 'bg-slate-200/60 dark:bg-slate-800 text-slate-600 dark:text-slate-300 hover:bg-slate-300'
                }`}>
                {cat}
              </button>
            ))}
          </div>

          {/* PRODUCTS GRID */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {filteredProducts.map((product) => {
              const isWishlisted = wishlist.includes(product.id);
              return (
                <div
                  key={product.id}
                  className="group rounded-3xl bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 overflow-hidden shadow-sm hover:shadow-xl transition-all duration-300 flex flex-col">
                  
                  <div className="relative aspect-square overflow-hidden bg-slate-100 dark:bg-slate-800 cursor-pointer" onClick={() => setActiveProductModal(product)}>
                    <img
                      src={product.images[0]}
                      alt={product.title}
                      className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                    />

                    <div className="absolute top-3 left-3">
                      <span className={`px-2.5 py-1 rounded-full text-[10px] font-black uppercase tracking-wider shadow-md ${
                        product.source === 'amazon'
                          ? 'bg-amber-400 text-slate-950'
                          : product.source === 'aliexpress'
                          ? 'bg-red-500 text-white'
                          : product.source === 'alibaba'
                          ? 'bg-orange-500 text-white'
                          : 'bg-blue-600 text-white'
                      }`}>
                        {product.source}
                      </span>
                    </div>

                    <button
                      onClick={(e) => { e.stopPropagation(); toggleWishlist(product.id); }}
                      className="absolute top-3 right-3 p-2 rounded-full bg-white/80 dark:bg-slate-900/80 backdrop-blur text-slate-700 dark:text-slate-200 hover:scale-110 transition-all">
                      <Heart className={`w-4 h-4 ${isWishlisted ? 'fill-red-500 text-red-500' : ''}`} />
                    </button>

                    {product.isFlashSale && (
                      <div className="absolute bottom-3 left-3 bg-red-600 text-white text-[11px] font-black px-2 py-0.5 rounded-lg flex items-center gap-1 shadow-lg">
                        <Zap className="w-3 h-3 fill-white" />
                        <span>-{product.flashDiscountPercent}% OFF</span>
                      </div>
                    )}
                  </div>

                  <div className="p-5 flex-1 flex flex-col justify-between">
                    <div>
                      <div className="flex items-center justify-between text-xs text-slate-400 mb-1">
                        <span>{product.category}</span>
                        <div className="flex items-center gap-1 text-amber-400 font-bold">
                          <Star className="w-3.5 h-3.5 fill-amber-400" />
                          <span>{product.rating}</span>
                          <span className="text-slate-400">({product.reviewsCount})</span>
                        </div>
                      </div>

                      <h3 
                        onClick={() => setActiveProductModal(product)}
                        className="font-bold text-sm text-slate-900 dark:text-slate-100 hover:text-blue-600 dark:hover:text-blue-400 cursor-pointer line-clamp-2 transition-colors">
                        {product.title}
                      </h3>

                      <p className="text-xs text-slate-400 mt-1 line-clamp-1">
                        Sold by <span className="font-semibold text-slate-600 dark:text-slate-300">{product.sellerName}</span>
                      </p>
                    </div>

                    <div className="mt-4 pt-4 border-t border-slate-100 dark:border-slate-800/80 flex items-center justify-between">
                      <div>
                        <div className="flex items-baseline gap-2">
                          <span className="text-lg font-black text-slate-900 dark:text-white">
                            ${product.price.toFixed(2)}
                          </span>
                          {product.originalPrice && (
                            <span className="text-xs text-slate-400 line-through">
                              ${product.originalPrice.toFixed(2)}
                            </span>
                          )}
                        </div>
                        <span className="text-[10px] text-emerald-500 font-bold block">
                          {product.estimatedDelivery}
                        </span>
                      </div>

                      <button
                        onClick={() => addToCart(product)}
                        className="p-2.5 rounded-xl bg-slate-900 dark:bg-blue-600 text-white hover:bg-blue-700 transition-colors shadow-md">
                        <Plus className="w-4 h-4" />
                      </button>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </main>
      )}

      {/* 5. PRODUCT DETAIL MODAL (PDP) */}
      {activeProductModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm">
          <div className="bg-white dark:bg-slate-900 max-w-3xl w-full rounded-3xl overflow-hidden shadow-2xl border border-slate-200 dark:border-slate-800 max-h-[90vh] flex flex-col">
            <div className="flex justify-between items-center p-4 border-b border-slate-200 dark:border-slate-800">
              <div className="flex items-center gap-2">
                <span className="text-xs font-black uppercase tracking-wider px-2 py-0.5 rounded bg-blue-100 dark:bg-blue-900/40 text-blue-700 dark:text-blue-300">
                  {activeProductModal.source.toUpperCase()} SOURCED
                </span>
                <span className="text-xs text-slate-400 font-mono">SKU: {activeProductModal.sku}</span>
              </div>
              <button 
                onClick={() => setActiveProductModal(null)}
                className="p-1.5 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800 text-slate-400">
                <X className="w-5 h-5" />
              </button>
            </div>

            <div className="p-6 overflow-y-auto custom-scrollbar grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-3">
                <div className="aspect-square rounded-2xl overflow-hidden bg-slate-100 dark:bg-slate-800">
                  <img src={activeProductModal.images[0]} alt={activeProductModal.title} className="w-full h-full object-cover" />
                </div>
              </div>

              <div className="flex flex-col justify-between">
                <div>
                  <h2 className="text-xl font-black text-slate-900 dark:text-white leading-snug">
                    {activeProductModal.title}
                  </h2>

                  <div className="flex items-center gap-2 my-2 text-xs">
                    <div className="flex text-amber-400 font-bold">
                      <Star className="w-4 h-4 fill-amber-400" />
                      <span className="ml-1">{activeProductModal.rating}</span>
                    </div>
                    <span className="text-slate-400">({activeProductModal.reviewsCount} verified reviews)</span>
                  </div>

                  <div className="flex items-baseline gap-3 my-4">
                    <span className="text-3xl font-black text-blue-600 dark:text-blue-400">
                      ${activeProductModal.price.toFixed(2)}
                    </span>
                    {activeProductModal.originalPrice && (
                      <span className="text-sm text-slate-400 line-through">
                        ${activeProductModal.originalPrice.toFixed(2)}
                      </span>
                    )}
                  </div>

                  <p className="text-xs text-slate-500 dark:text-slate-400 leading-relaxed mb-4">
                    {activeProductModal.description}
                  </p>

                  <div className="text-xs font-semibold text-emerald-500 mb-4 flex items-center gap-1.5">
                    <CheckCircle2 className="w-4 h-4" />
                    <span>In Stock: {activeProductModal.stockQuantity} units available</span>
                  </div>

                  <div className="p-3 rounded-2xl bg-slate-50 dark:bg-slate-800/60 border border-slate-200 dark:border-slate-700/60 text-xs space-y-1.5 mb-6">
                    <div className="flex items-center gap-2 text-slate-600 dark:text-slate-300">
                      <Truck className="w-3.5 h-3.5 text-blue-500" />
                      <span>Delivery: {activeProductModal.estimatedDelivery}</span>
                    </div>
                    <div className="flex items-center gap-2 text-slate-600 dark:text-slate-300">
                      <ShieldCheck className="w-3.5 h-3.5 text-emerald-500" />
                      <span>30-Day Hassle-Free Returns & Full Refund Guarantee</span>
                    </div>
                  </div>
                </div>

                <div className="space-y-3 pt-4 border-t border-slate-200 dark:border-slate-800">
                  <button
                    onClick={() => { addToCart(activeProductModal); setActiveProductModal(null); }}
                    className="w-full py-3 rounded-2xl bg-blue-600 hover:bg-blue-700 text-white font-black text-sm flex items-center justify-center gap-2 shadow-lg shadow-blue-600/30 transition-all">
                    <ShoppingBag className="w-4 h-4" />
                    <span>Add to Zexo Cart (${activeProductModal.price.toFixed(2)})</span>
                  </button>

                  {activeProductModal.externalAffiliateUrl && (
                    <a
                      href={activeProductModal.externalAffiliateUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="w-full py-2.5 rounded-2xl border border-slate-300 dark:border-slate-700 hover:bg-slate-100 dark:hover:bg-slate-800 text-slate-600 dark:text-slate-300 font-bold text-xs flex items-center justify-center gap-2 transition-colors">
                      <span>View on {activeProductModal.source.toUpperCase()}</span>
                      <ExternalLink className="w-3.5 h-3.5" />
                    </a>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* 6. SLIDE-OVER CART DRAWER */}
      {isCartOpen && (
        <div className="fixed inset-0 z-50 overflow-hidden">
          <div className="absolute inset-0 bg-black/50 backdrop-blur-sm" onClick={() => setIsCartOpen(false)} />
          <div className="absolute inset-y-0 right-0 max-w-full flex pl-10">
            <div className="w-screen max-w-md bg-white dark:bg-slate-900 shadow-2xl flex flex-col border-l border-slate-200 dark:border-slate-800">
              
              <div className="p-5 border-b border-slate-200 dark:border-slate-800 flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <ShoppingBag className="w-5 h-5 text-blue-600" />
                  <h2 className="text-lg font-black">Your Shopping Cart</h2>
                  <span className="text-xs bg-slate-100 dark:bg-slate-800 px-2 py-0.5 rounded-full font-bold">
                    {cartItems.reduce((acc, i) => acc + i.quantity, 0)} items
                  </span>
                </div>
                <button onClick={() => setIsCartOpen(false)} className="p-1 rounded-lg text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800">
                  <X className="w-5 h-5" />
                </button>
              </div>

              {/* Free Shipping Progress Indicator */}
              <div className="p-4 bg-blue-50 dark:bg-blue-950/40 border-b border-blue-100 dark:border-blue-900/40 text-xs">
                {cartSubtotal >= 150 ? (
                  <p className="text-emerald-500 font-bold flex items-center gap-1.5">
                    <CheckCircle2 className="w-4 h-4" />
                    <span>Unlocked Free Global Tracked Shipping!</span>
                  </p>
                ) : (
                  <div>
                    <p className="text-slate-600 dark:text-slate-300">
                      Add <span className="font-black text-blue-600 dark:text-blue-400">${(150 - cartSubtotal).toFixed(2)}</span> more to qualify for Free Global Shipping!
                    </p>
                    <div className="w-full h-1.5 bg-slate-200 dark:bg-slate-700 rounded-full mt-2 overflow-hidden">
                      <div className="h-full bg-blue-600 transition-all duration-300" style={{ width: `${Math.min(100, (cartSubtotal / 150) * 100)}%` }} />
                    </div>
                  </div>
                )}
              </div>

              {/* Cart List */}
              <div className="flex-1 overflow-y-auto p-5 space-y-4 custom-scrollbar">
                {cartItems.length === 0 ? (
                  <div className="text-center py-16 text-slate-400">
                    <ShoppingBag className="w-12 h-12 mx-auto mb-3 opacity-30" />
                    <p className="font-bold">Your cart is empty</p>
                    <p className="text-xs mt-1">Discover items from Amazon, AliExpress & Local Stores.</p>
                  </div>
                ) : (
                  cartItems.map((item) => (
                    <div key={item.product.id} className="flex gap-4 p-3 rounded-2xl bg-slate-50 dark:bg-slate-800/40 border border-slate-200 dark:border-slate-800">
                      <img src={item.product.images[0]} alt="" className="w-16 h-16 object-cover rounded-xl bg-slate-200" />
                      <div className="flex-1">
                        <h4 className="text-xs font-bold line-clamp-1">{item.product.title}</h4>
                        <span className="text-[10px] text-slate-400 block mt-0.5">Source: {item.product.source.toUpperCase()}</span>
                        <div className="flex items-center justify-between mt-2">
                          <span className="font-black text-sm text-blue-600 dark:text-blue-400">
                            ${(item.product.price * item.quantity).toFixed(2)}
                          </span>
                          <div className="flex items-center gap-2 bg-white dark:bg-slate-800 rounded-lg border border-slate-200 dark:border-slate-700 px-2 py-0.5 text-xs">
                            <button onClick={() => updateCartQty(item.product.id, -1)} className="p-0.5 hover:text-blue-600"><Minus className="w-3 h-3" /></button>
                            <span className="font-bold">{item.quantity}</span>
                            <button onClick={() => updateCartQty(item.product.id, 1)} className="p-0.5 hover:text-blue-600"><Plus className="w-3 h-3" /></button>
                          </div>
                          <button onClick={() => removeFromCart(item.product.id)} className="text-slate-400 hover:text-red-500">
                            <Trash2 className="w-3.5 h-3.5" />
                          </button>
                        </div>
                      </div>
                    </div>
                  ))
                )}
              </div>

              {/* Cart Footer */}
              {cartItems.length > 0 && (
                <div className="p-5 border-t border-slate-200 dark:border-slate-800 space-y-3 bg-white dark:bg-slate-900">
                  <div className="space-y-1.5 text-xs">
                    <div className="flex justify-between text-slate-500">
                      <span>Subtotal</span>
                      <span>${cartSubtotal.toFixed(2)}</span>
                    </div>
                    {discountAmount > 0 && (
                      <div className="flex justify-between text-emerald-500 font-bold">
                        <span>Promo Code (ZEXO10)</span>
                        <span>-${discountAmount.toFixed(2)}</span>
                      </div>
                    )}
                    <div className="flex justify-between text-slate-500">
                      <span>Global Shipping</span>
                      <span>{shippingCost === 0 ? 'FREE' : `$${shippingCost.toFixed(2)}`}</span>
                    </div>
                    <div className="flex justify-between text-base font-black pt-2 border-t border-slate-200 dark:border-slate-800 text-slate-900 dark:text-white">
                      <span>Total</span>
                      <span className="text-blue-600 dark:text-blue-400">${orderTotal.toFixed(2)}</span>
                    </div>
                  </div>

                  <button
                    onClick={() => { setIsCartOpen(false); setIsCheckoutOpen(true); }}
                    className="w-full py-3.5 rounded-2xl bg-blue-600 hover:bg-blue-700 text-white font-black text-sm flex items-center justify-center gap-2 shadow-lg shadow-blue-600/30 transition-all">
                    <span>Proceed to Multi-Step Checkout</span>
                    <ArrowRight className="w-4 h-4" />
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      )}

      {/* 7. MULTI-STEP CHECKOUT MODAL */}
      {isCheckoutOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm">
          <div className="bg-white dark:bg-slate-900 max-w-xl w-full rounded-3xl overflow-hidden shadow-2xl border border-slate-200 dark:border-slate-800">
            <div className="p-5 border-b border-slate-200 dark:border-slate-800 flex justify-between items-center">
              <div>
                <h3 className="font-black text-lg">Secure Zexo Global Checkout</h3>
                <span className="text-xs text-slate-400">Step {checkoutStep} of 3: {checkoutStep === 1 ? 'Shipping Address' : checkoutStep === 2 ? 'Fulfillment & Freight' : 'Payment Gateway'}</span>
              </div>
              <button onClick={() => setIsCheckoutOpen(false)} className="p-1 rounded-lg text-slate-400"><X className="w-5 h-5" /></button>
            </div>

            <div className="p-6">
              {checkoutStep === 1 && (
                <div className="space-y-4">
                  <div>
                    <label className="text-xs font-bold text-slate-400 block mb-1">Full Recipient Name</label>
                    <input
                      type="text"
                      value={shippingAddress.fullName}
                      onChange={(e) => setShippingAddress({ ...shippingAddress, fullName: e.target.value })}
                      className="w-full px-4 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-sm font-semibold"
                    />
                  </div>
                  <div className="grid grid-cols-2 gap-3">
                    <div>
                      <label className="text-xs font-bold text-slate-400 block mb-1">Email</label>
                      <input
                        type="email"
                        value={shippingAddress.email}
                        onChange={(e) => setShippingAddress({ ...shippingAddress, email: e.target.value })}
                        className="w-full px-4 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-sm font-semibold"
                      />
                    </div>
                    <div>
                      <label className="text-xs font-bold text-slate-400 block mb-1">Phone</label>
                      <input
                        type="text"
                        value={shippingAddress.phone}
                        onChange={(e) => setShippingAddress({ ...shippingAddress, phone: e.target.value })}
                        className="w-full px-4 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-sm font-semibold"
                      />
                    </div>
                  </div>
                  <div>
                    <label className="text-xs font-bold text-slate-400 block mb-1">Street Address</label>
                    <input
                      type="text"
                      value={shippingAddress.street}
                      onChange={(e) => setShippingAddress({ ...shippingAddress, street: e.target.value })}
                      className="w-full px-4 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-sm font-semibold"
                    />
                  </div>
                  <div className="grid grid-cols-2 gap-3">
                    <div>
                      <label className="text-xs font-bold text-slate-400 block mb-1">City</label>
                      <input
                        type="text"
                        value={shippingAddress.city}
                        onChange={(e) => setShippingAddress({ ...shippingAddress, city: e.target.value })}
                        className="w-full px-4 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-sm font-semibold"
                      />
                    </div>
                    <div>
                      <label className="text-xs font-bold text-slate-400 block mb-1">Country</label>
                      <input
                        type="text"
                        value={shippingAddress.country}
                        onChange={(e) => setShippingAddress({ ...shippingAddress, country: e.target.value })}
                        className="w-full px-4 py-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-sm font-semibold"
                      />
                    </div>
                  </div>

                  <button
                    onClick={() => setCheckoutStep(2)}
                    className="w-full py-3 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl text-sm transition-all mt-4">
                    Continue to Delivery Method
                  </button>
                </div>
              )}

              {checkoutStep === 2 && (
                <div className="space-y-4">
                  <div 
                    onClick={() => setDeliveryMethod('express')}
                    className={`p-4 rounded-2xl border cursor-pointer transition-all ${
                      deliveryMethod === 'express' ? 'border-blue-600 bg-blue-50/20' : 'border-slate-200 dark:border-slate-800'
                    }`}>
                    <div className="flex justify-between">
                      <span className="font-bold text-sm">Priority Air Cargo & Express Courier</span>
                      <span className="font-black text-sm text-blue-600">${shippingCost.toFixed(2)}</span>
                    </div>
                    <p className="text-xs text-slate-400 mt-1">Direct from Amazon / Overseas Hubs. 3-6 business days with door-to-door tracking.</p>
                  </div>

                  <div 
                    onClick={() => setDeliveryMethod('standard')}
                    className={`p-4 rounded-2xl border cursor-pointer transition-all ${
                      deliveryMethod === 'standard' ? 'border-blue-600 bg-blue-50/20' : 'border-slate-200 dark:border-slate-800'
                    }`}>
                    <div className="flex justify-between">
                      <span className="font-bold text-sm">Standard Tracked Shipping</span>
                      <span className="font-black text-sm text-emerald-500">FREE</span>
                    </div>
                    <p className="text-xs text-slate-400 mt-1">7-12 business days via postal network.</p>
                  </div>

                  <div className="flex gap-2">
                    <button onClick={() => setCheckoutStep(1)} className="w-1/3 py-3 border border-slate-300 dark:border-slate-700 rounded-xl text-xs">Back</button>
                    <button onClick={() => setCheckoutStep(3)} className="w-2/3 py-3 bg-blue-600 text-white font-bold rounded-xl text-xs">
                      Continue to Payment
                    </button>
                  </div>
                </div>
              )}

              {checkoutStep === 3 && (
                <div className="space-y-4">
                  <div className="grid grid-cols-3 gap-2">
                    {['card', 'upi', 'cod'].map((pm) => (
                      <button
                        key={pm}
                        onClick={() => setPaymentMethod(pm as any)}
                        className={`py-2 px-3 rounded-xl border text-xs font-bold capitalize ${
                          paymentMethod === pm ? 'border-blue-600 bg-blue-50/20 text-blue-600' : 'border-slate-200 dark:border-slate-800'
                        }`}>
                        {pm === 'card' ? 'Card' : pm === 'upi' ? 'UPI / Wallet' : 'Cash On Delivery'}
                      </button>
                    ))}
                  </div>

                  <div className="p-4 rounded-2xl bg-slate-50 dark:bg-slate-800 text-xs space-y-1">
                    <div className="flex justify-between font-bold">
                      <span>Total Amount Payable:</span>
                      <span className="text-blue-500 text-sm font-black">${orderTotal.toFixed(2)}</span>
                    </div>
                    <p className="text-[10px] text-slate-400">Order will be relayed to suppliers and tracking code issued immediately.</p>
                  </div>

                  <div className="flex gap-2">
                    <button onClick={() => setCheckoutStep(2)} className="w-1/3 py-3 border border-slate-300 dark:border-slate-700 rounded-xl text-xs">Back</button>
                    <button onClick={handlePlaceOrder} className="w-2/3 py-3 bg-emerald-600 hover:bg-emerald-700 text-white font-black rounded-xl text-xs shadow-lg shadow-emerald-600/30">
                      Confirm & Place Order
                    </button>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      )}

      {/* 8. FOOTER */}
      <footer className="mt-20 border-t border-slate-200 dark:border-slate-800 py-10 bg-white dark:bg-slate-900/60 text-xs text-slate-400">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-wrap justify-between items-center gap-4">
          <div className="flex items-center gap-2">
            <span className="font-black text-slate-900 dark:text-white text-sm">ZEXO GLOBAL</span>
            <span>© 2026 Zexo E-Commerce Platform. All rights reserved.</span>
          </div>
          <div className="flex items-center gap-6">
            <span>Amazon PA-API v5</span>
            <span>AliExpress Open Platform</span>
            <span>Alibaba B2B Trade</span>
            <span>Verified Local Artisans</span>
          </div>
        </div>
      </footer>
    </div>
  );
                }
