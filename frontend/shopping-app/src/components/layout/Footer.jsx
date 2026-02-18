import "./Footer.css"

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-container">

        <div className="footer-section">
          <h3>ShopEasy</h3>
          <p>Your trusted online shopping platform.</p>
        </div>

        <div className="footer-section">
          <h4>Quick Links</h4>
          <p>Home</p>
          <p>Cart</p>
        </div>

        <div className="footer-section">
          <h4>Contact</h4>
          <p>Email: support@shopeasy.com</p>
          <p>Phone: +91 98765 43210</p>
        </div>

      </div>

      <div className="footer-bottom">
        © {new Date().getFullYear()} ShopEasy. All rights reserved.
      </div>
    </footer>
  )
}

export default Footer
