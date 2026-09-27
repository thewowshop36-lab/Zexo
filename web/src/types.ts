export type MarketplaceSource = 'amazon' | 'aliexpress' | 'alibaba' | 'local';

export interface Product {
  id: string;
  sku: string;
  title: string;
  description: string;
  category: string;
  price: number;
  originalPrice: number;
  rating: number;
  reviewsCount: number;
  source: MarketplaceSource;
  sellerName: string;
  stockQuantity: number;
  images: string[];
  isFlashSale: boolean;
  flashDiscountPercent?: number;
  externalAffiliateUrl?: string;
  estimatedDelivery: string;
  variants?: {
    colors: string[];
    sizes: string[];
  };
}

export interface CartItem {
  product: Product;
  quantity: number;
  selectedColor?: string;
  selectedSize?: string;
}

export interface ShippingAddress {
  fullName: string;
  email: string;
  phone: string;
  street: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
}

export interface Order {
  id: string;
  orderCode: string;
  date: string;
  items: CartItem[];
  subtotal: number;
  discount: number;
  shipping: number;
  total: number;
  paymentMethod: string;
  shippingAddress: ShippingAddress;
  status: 'Order Placed' | 'Supplier Confirmed' | 'Shipped via Air Cargo' | 'Out for Delivery' | 'Delivered';
  stepIndex: number;
  trackingNumber: string;
  carrier: string;
  primarySource: MarketplaceSource;
}
