;; Define Rule: Find all the offers that are able to be satisfied by the seller and create and offer with the relevant information
(defrule can-sell
   (seller (seller-id ?id))
   (order (seller-id ?id) (order-id ?orderId) (product $?product))
   (product (name $?product) (product-id ?productId))
   (product-seller (seller-id ?id) (product-id ?productId) (price ?price))
   =>
   (assert (offer (order-id ?orderId) (product-id ?productId) (product $?product) (price ?price)))
)

;; Define Rule: Define a rule for a order of iphone 13 and Banamex card
(defrule iPhone13-Banamex 
   (order (order-id ?orderId) (card banamex))
   (purchase (order-id ?orderId) (product $? iphone 13 $?))
=>
(assert (purchase-msg (order-id ?orderId) (message Obtienes 24 meses sin intereses))))

;; Define a rule for a order of note 12 and Liverpool card
(defrule note-liverpool 
   (order (order-id ?orderId) (card liverpool))
   (purchase (order-id ?orderId) (product $? samsung note 12 $?))
=>
   (assert (purchase-msg (order-id ?orderId) (message Obtienes 12 meses sin intereses))))

;; Define a rule for a purchase of an iphone and macbook air with card payment method 
(defrule mac-iphone 
   (order (order-id ?orderId) (card contado))
   (purchase (order-id ?orderId) (product $? iphone 13 $?))  
   (purchase (order-id ?orderId) (product $? macbook air $?))  
   => 
   (assert (purchase-msg (order-id ?orderId) (message Obtienes 100 pesos en vales por cada 1000 pesos de compra)))
)

(defrule purchase-smartphone 
   (order (order-id ?orderId))
   (purchase (order-id ?orderId) (product $?product))  
   (product (name $?product) (category smartphones))  
   => 
   (assert (purchase-msg (order-id ?orderId) (message Por tu compra puedes obtener una funda y mica con 15% de descuento)))
)

(defrule purchase-mercedez 
   (order (order-id ?orderId))
   (purchase (order-id ?orderId) (product $? mercedez benz $?))  
   => 
   (assert (purchase-msg (order-id ?orderId) (message Seguro Gratis)))
)