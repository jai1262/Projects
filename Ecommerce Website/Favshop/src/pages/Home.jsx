import React from 'react'
import Navbar from '../components/Navbar'
import Announcement from '../components/Announcement'
import Slider from '../components/Slider'
import Categories from '../components/Categories'
import Newsletter from '../components/Newsletter'
import Footer from '../components/Footer'
import Products from '../components/Products'
import MensFashion from '../components/MensFashion'
import WomensFashion from '../components/WomensFashion'
import AllProds from '../components/AllProds'

function Home() {
  return (
    <div>
        <Announcement />
        <Navbar />
        <Slider />
        <Categories />
        <Products />
        <WomensFashion />
        <MensFashion />
        <AllProds />
        <Newsletter />
        <Footer />
    </div>
  )
}

export default Home