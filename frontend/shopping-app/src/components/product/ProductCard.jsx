import "./ProductCard.css"

function ProductCard({ product,inventory }) {

    const status = inventory?.status;

  return (
    <div className="product-card">
      <img src={product.imageUrl} alt={product.name} />
      <h3>{product.name}</h3>
      <p className="description">{product.description}</p>
      <p className="price">₹ {product.price}</p>
      
        <p className={
        status === "IN_STOCK"
          ? "in-stock"
          : status === "LOW_STOCK"
          ? "low-stock"
          : "out-of-stock"
      }>
        {inventory ? status.replace("_", " ") : "Checking..."}
      </p>

      <button disabled={status === "OUT_OF_STOCK"}>
        Add to Cart
      </button>
    </div>
  )
}

export default ProductCard
