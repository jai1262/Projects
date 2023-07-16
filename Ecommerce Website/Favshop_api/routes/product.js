const router = require("express").Router();
const Product = require("../models/Product");
const  { verifyToken, verifyTokenAndAuthorization, verifyTokenAndAdmin } = require("./verifyToken");
/**url : localhost:5000/api/users/usertest */
/*
router.get("/usertest", (req, res) => {
    res.send("user test is successful");
});

router.post("/userposttest", (req, res) => {
    const username = req.body.username;
    console.log(username);
    res.send("Your name is " + username);
});
*/

//CREATE

router.post("/", verifyTokenAndAdmin, async(req, res) => {
    const newProduct = new Product(req.body);

    try {
        const savedProduct = await newProduct.save();       //create a new record in the products table
        res.status(200).json(savedProduct);
    } catch(err) {
        res.status(500).json(err);
    }
})


//UPDATE
router.put("/:id", verifyTokenAndAdmin, async (req, res) => {
    try {
        const updatedProduct = await Product.findByIdAndUpdate(req.params.id, {
            $set: req.body},
            {new: true}
        );
        res.status(200).json(updatedProduct);
    } catch (err) {
        res.status(500).json("Error" + err);
    }
});

//DELETE
router.delete("/:id", verifyTokenAndAdmin, async(req, res) => {
    try {
        await Product.findByIdAndDelete(req.params.id);
        res.status(200).json("Product has been deleted...");
    } catch (err) {
        res.status(500).json(err);
    }
});

//GET PRODUCT
//anyone can generate get request for a product
router.get("/find/:id", async(req, res) => {
    try {
        const prod = await Product.findById(req.params.id);
        res.status(200).json(prod);
    } catch (err) {
        res.status(500).json(err);
    }
});

//GET ALL PRODUCTS
router.get("/", async(req, res) => {
    const queryNew = req.query.new;
    const queryCategory = req.query.category;
    console.log("Category : " + req.query.category + "\n\n");

    try {
        let prodArray;
        if(queryNew) {
            prodArray = await Product.find().sort({createdAt: -1})      //.limit(5)
        } else if(queryCategory) {
            prodArray = await Product.find({categories: {
                $in: [queryCategory],
            },
        });
        } else {
            prodArray = await Product.find();
        }
        res.status(200).json(prodArray);
    } catch (err) {
        res.status(500).json(err);
    }
});

module.exports = router;