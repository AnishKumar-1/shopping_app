import "./ProductCard.css"

function ProductCard({ product }) {
  return (
    <div className="product-card">
      <img src={product.imageUrl} alt={product.name} />
      <h3>{product.name}</h3>
      <p className="description">{product.description}</p>
      <p className="price">₹ {product.price}</p>

      <p className={product.inStock ? "in-stock" : "out-of-stock"}>
        {product.inStock ? "In Stock" : "Out of Stock"}
      </p>

      <button disabled={!product.inStock}>
        Add to Cart
      </button>
    </div>
  )
}

export default ProductCard
