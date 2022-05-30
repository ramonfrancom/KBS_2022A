(deffacts products 
 	(product (product-id 1) (name iphone 7) (category smartphones))
 	(product (product-id 2) (name iphone 13) (category smartphones))
 	(product (product-id 3) (name samsung note 12) (category smartphones))
 	(product (product-id 4) (name macbook air) (category computers))
        (product (product-id 5) (name mercedez benz) (category cars))
)

(deffacts sellers
  (seller (seller-id amazon1))
  (seller (seller-id amazon2))
  (seller (seller-id alibaba)) 
)  	 

(deffacts product-sellers
	(product-seller (product-id 1) (seller-id amazon1) (price 1000))
	(product-seller (product-id 1) (seller-id alibaba) (price 1100))
	(product-seller (product-id 2) (seller-id amazon1) (price 1500))
	(product-seller (product-id 2) (seller-id amazon2) (price 1450))
	(product-seller (product-id 3) (seller-id amazon1) (price 1800))
	(product-seller (product-id 3) (seller-id alibaba) (price 1900))
	(product-seller (product-id 4) (seller-id amazon2) (price 1900))
        (product-seller (product-id 5) (seller-id amazon2) (price 1900))
)
