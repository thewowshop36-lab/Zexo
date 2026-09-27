import { createClient } from '@supabase/supabase-js';

// Supabase client instance using environment variables or safe fallbacks
const supabaseUrl = import.meta.env?.VITE_SUPABASE_URL || 'https://xyzcompany.supabase.co';
const supabaseAnonKey = import.meta.env?.VITE_SUPABASE_ANON_KEY || 'public-anon-key-placeholder';

export const supabase = createClient(supabaseUrl, supabaseAnonKey);
