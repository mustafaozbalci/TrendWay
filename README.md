# TrendWay
User: Kullanıcıyı temsil eder ve her kullanıcı yalnızca bir role sahip olabilir. Ayrıca, her kullanıcının bir alışveriş sepeti olabilir. Bir kullanıcı birden fazla sipariş verebilir ve her sipariş için ödeme yapabilir.

Role: Kullanıcılara çeşitli yetkiler tanımlar.

Company: Ürünleri üreten veya satan şirketlerdir.

Category: Ürünlerin kategorilerini tanımlar.

Product: Satılan ürünlerdir. Birden fazla kategoriye ait olabilir ve bir şirket tarafından üretilmiş olabilir.

Cart: Kullanıcının sepetini tanımlar.

CartItem: Sepetteki her bir ürünü tanımlar.

Order: Kullanıcının gerçekleştirdiği siparişleri tanımlar. Bir kullanıcı birden fazla sipariş verebilir. Siparişlerin sipariş tarihi ve içerdikleri ürünler (OrderItem) bulunur.

OrderItem: Siparişteki her bir ürünü tanımlar. Her OrderItem, bir ürünü (Product) ve miktarını içerir.

Payment: Siparişler için yapılan ödemeleri tanımlar. Her ödeme bir siparişe (Order) bağlıdır ve ödeme yöntemi, tutar, ödeme tarihi ve durum bilgilerini içerir.
