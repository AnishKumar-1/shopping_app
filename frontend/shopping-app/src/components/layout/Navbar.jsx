import { Link } from "react-router-dom"
import "./Navbar.css"

function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar-container">
        
        <div className="navbar-logo">
          <Link to="/">ShopEasy</Link>
        </div>

        <div className="navbar-links">
          <Link to="/">Home</Link>
          <Link to="/cart">Cart</Link>
        </div>

      </div>
    </nav>
  )
}

export default Navbar
