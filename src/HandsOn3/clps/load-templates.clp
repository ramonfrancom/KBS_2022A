(deftemplate seller
  (slot seller-id)
)

(deftemplate product
  (slot product-id)
  (multislot name)
  (slot category)
)

(deftemplate product-seller
  (slot seller-id)
  (slot product-id)
  (slot price)
)

(deftemplate order
  (slot order-id)
  (slot seller-id)
  (slot card)
  (multislot product)
)

(deftemplate offer
  (slot order-id)
  (slot product-id)
  (multislot product)
  (slot price)
)

(deftemplate purchase
  (slot order-id)
  (multislot product)
)


(deftemplate purchase-msg
  (slot order-id)
  (multislot message)
)
