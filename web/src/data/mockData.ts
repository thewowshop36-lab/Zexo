import { Product } from '../types';

export const INITIAL_PRODUCTS: Product[] = [
  {
    id: 'prod-1',
    sku: 'AMZ-SNY-1005',
    title: 'Sony WH-1000XM5 Wireless Noise Canceling Headphones',
    description: 'Industry-leading noise canceling with Auto NC Optimizer, crystal clear hands-free calling, and up to 30 hours battery life with quick charging.',
    category: 'Electronics',
    price: 348.00,
    originalPrice: 399.99,
    rating: 4.8,
    reviewsCount: 1420,
    source: 'amazon',
    sellerName: 'Amazon Prime Direct Global',
    stockQuantity: 42,
    images: [
      'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800',
      'https://images.unsplash.com/photo-1484704849700-f032a568e944?w=800'
    ],
    isFlashSale: true,
    flashDiscountPercent: 15,
    externalAffiliateUrl: 'https://amazon.com/dp/B09XS7JWHH?tag=zexo-global-20',
    estimatedDelivery: '2-4 Days Priority Air Cargo',
    variants: {
      colors: ['Midnight Black', 'Silver Cloud', 'Smoky Navy'],
      sizes: ['Standard Over-Ear']
    }
  },
  {
    id: 'prod-2',
    sku: 'ALI-KB-8821',
    title: 'CyberPunk RGB Mechanical Gaming Keyboard Hot-Swappable',
    description: '75% Layout wireless mechanical keyboard featuring gasket-mounted acoustic dampening, custom linear switches, and dynamic per-key RGB backlighting.',
    category: 'Gadgets',
    price: 69.50,
    originalPrice: 119.00,
    rating: 4.9,
    reviewsCount: 840,
    source: 'aliexpress',
    sellerName: 'Shenzhen E-Tech Store (AliExpress Verified)',
    stockQuantity: 150,
    images: [
      'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=800',
      'https://images.unsplash.com/photo-1595225476474-87563907a212?w=800'
    ],
    isFlashSale: true,
    flashDiscountPercent: 42,
    externalAffiliateUrl: 'https://aliexpress.com/item/10050064219.html',
    estimatedDelivery: '7-12 Days Tracked Standard',
    variants: {
      colors: ['Neon Cyan', 'Obsidian Purple', 'Chalk White'],
      sizes: ['Red Switches', 'Brown Tactile']
    }
  },
  {
    id: 'prod-3',
    sku: 'B2B-WTC-502',
    title: 'Titanium Smart Watch Ultra (Wholesale MOQ: 2 pcs)',
    description: 'Aircraft-grade titanium casing with sapphire glass, ECG heart monitoring, dual-frequency GPS, and up to 100m water resistance. Ideal for bulk ordering.',
    category: 'Gadgets',
    price: 45.00,
    originalPrice: 89.00,
    rating: 4.7,
    reviewsCount: 310,
    source: 'alibaba',
    sellerName: 'Guangdong Precision Electronics Co., Ltd',
    stockQuantity: 2500,
    images: [
      'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800',
      'https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?w=800'
    ],
    isFlashSale: false,
    externalAffiliateUrl: 'https://alibaba.com/product-detail/titanium-smart-watch.html',
    estimatedDelivery: '10-15 Days Global B2B Freight',
    variants: {
      colors: ['Raw Titanium', 'Midnight DLC'],
      sizes: ['49mm Case']
    }
  },
  {
    id: 'prod-4',
    sku: 'LOC-BAG-990',
    title: 'Handcrafted Minimalist Full-Grain Leather Backpack',
    description: 'Made by verified local master artisans from vegetable-tanned full-grain leather. Features a 16-inch padded laptop compartment and brass hardware.',
    category: 'Fashion',
    price: 185.00,
    originalPrice: 220.00,
    rating: 5.0,
    reviewsCount: 68,
    source: 'local',
    sellerName: 'Artisan Heritage Leatherworks',
    stockQuantity: 18,
    images: [
      'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=800',
      'https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=800'
    ],
    isFlashSale: false,
    estimatedDelivery: '1-3 Days Local Express',
    variants: {
      colors: ['Cognac Tan', 'Espresso Dark', 'Classic Black'],
      sizes: ['20 Liters', '25 Liters']
    }
  },
  {
    id: 'prod-5',
    sku: 'AMZ-SPK-334',
    title: 'Echo Studio High-Fidelity Smart Speaker with 3D Audio',
    description: 'Immersive sound with Dolby Atmos and spatial audio processing technology. Alexa built-in for smart home automated controls.',
    category: 'Home & Living',
    price: 199.99,
    originalPrice: 219.99,
    rating: 4.6,
    reviewsCount: 2890,
    source: 'amazon',
    sellerName: 'Amazon Prime Direct Global',
    stockQuantity: 65,
    images: [
      'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=800'
    ],
    isFlashSale: false,
    externalAffiliateUrl: 'https://amazon.com/dp/B07G9Y3ZMC?tag=zexo-global-20',
    estimatedDelivery: '2-4 Days Priority Air Cargo',
    variants: {
      colors: ['Charcoal', 'Glacier White'],
      sizes: ['One Size']
    }
  },
  {
    id: 'prod-6',
    sku: 'LOC-CER-411',
    title: 'Ceramic Pour-Over Coffee Brewer & Dripper Set',
    description: 'Hand-thrown matte ceramic dripper with wooden collar and heat-resistant glass carafe. Includes 100 organic unbleached paper filters.',
    category: 'Home & Living',
    price: 38.00,
    originalPrice: 48.00,
    rating: 4.9,
    reviewsCount: 112,
    source: 'local',
    sellerName: 'Zenith Tableware Studio',
    stockQuantity: 34,
    images: [
      'https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=800'
    ],
    isFlashSale: true,
    flashDiscountPercent: 20,
    estimatedDelivery: '1-3 Days Local Express',
    variants: {
      colors: ['Matte Slate Grey', 'Sandy Oat'],
      sizes: ['600ml Carafe']
    }
  }
];
