const router = require("express").Router();
const Order = require("../models/Order");
const  { verifyToken, verifyTokenAndAuthorization, verifyTokenAndAdmin, verifyTokenAndAuthorization2 } = require("./verifyToken");


//CREATE

router.post("/", verifyToken, async(req, res) => {
    const newOrder = new Order(req.body);

    try {
        const savedOrder = await newOrder.save();       //create a new record in the cart table
        res.status(200).json(savedOrder);
    } catch(err) {
        res.status(500).json(err);
    }
})

//UPDATE
router.put("/:id", verifyTokenAndAdmin, async (req, res) => {
    try {
        const updatedOrder = await Order.findByIdAndUpdate(req.params.id, {
            $set: req.body},
            {new: true}
        );
        res.status(200).json(updatedOrder);
    } catch (err) {
        res.status(500).json("Error" + err);
    }
});

//DELETE
router.delete("/:id", verifyTokenAndAdmin, async(req, res) => {
    try {
        await Order.findByIdAndDelete(req.params.id);
        res.status(200).json("Order has been deleted...");
    } catch (err) {
        res.status(500).json(err);
    }
});

//GET User Order
//anyone can generate get request for a product
//find by user id not by order id
router.get("/find/:userId", verifyTokenAndAuthorization2, async(req, res) => {
    try {
        const order = await Order.find({userId: req.params.userId});
        res.status(200).json(order);
    } catch (err) {
        res.status(500).json(err);
    }
});

//GET ALL Orders
router.get("/", verifyTokenAndAdmin ,async(req, res) => {
    try {
        const order = await Order.find();
        res.status(200).json(order);
    } catch (err) {
        res.status(500).json(err);
    }
});

//GET MONTHLY INCOME
router.get("/income", verifyTokenAndAdmin, async(req, res) => {
    const date = new Date();            //1st september
    const lastMonth = new Date(date.setMonth(date.getMonth() - 1));     //1st august
    const prevLastMonth = new Date(new Date().setMonth(lastMonth.getMonth() - 1));      //1st july

    try {
        const income = await Order.aggregate([
            { $match: { createdAt: { $gte: prevLastMonth } } },
            {
                $project: {
                month: { $month: "$createdAt" },
                sales: "$amount",
                },
            },
            {
                $group: {
                    _id: "$month",
                    total: {$sum: "$sales"},
                },
            },
        ]);
        res.status(200).json(income);
    } catch(err) {
        res.status(500).json(err);
    }
});
module.exports = router;