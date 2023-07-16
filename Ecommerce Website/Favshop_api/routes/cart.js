const router = require("express").Router();
const Cart = require("../models/Cart");
const  { verifyToken, verifyTokenAndAuthorization, verifyTokenAndAdmin } = require("./verifyToken");

//CREATE
router.post("/", verifyToken, async(req, res) => {
    const newCart = new Cart(req.body);
    try {
        const savedCart = await newCart.save();       //create a new record in the cart table
        res.status(200).json(savedCart);
    } catch(err) {
        res.status(500).json(err);
    }
});

//UPDATE
router.put("/:id", verifyTokenAndAuthorization, async (req, res) => {
    try {
        const updatedCart = await Cart.findByIdAndUpdate(req.params.id, {
            $set: req.body},
            {new: true}
        );
        res.status(200).json(updatedCart);
    } catch (err) {
        res.status(500).json("Error" + err);
    }
});

//DELETE
router.delete("/:id", verifyTokenAndAuthorization, async(req, res) => {
    try {
        await Cart.findByIdAndDelete(req.params.id);
        res.status(200).json("Cart has been deleted...");
    } catch (err) {
        res.status(500).json(err);
    }
});

//GET User Cart
//anyone can generate get request for a product
router.get("/find/:userId", verifyTokenAndAuthorization, async(req, res) => {
    try {
        const cart = await Cart.findById({userId: req.params.userId});
        res.status(200).json(cart);
    } catch (err) {
        res.status(500).json(err);
    }
});

//GET ALL Carts
router.get("/", verifyTokenAndAdmin ,async(req, res) => {
    try {
        const carts = await Cart.find();
        res.status(200).json(carts);
    } catch (err) {
        res.status(500).json(err);
    }
});

module.exports = router;