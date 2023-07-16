const router = require("express").Router();
const User = require("../models/User");
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

//UPDATE
router.put("/:id", verifyTokenAndAuthorization, async (req, res) => {
    if(req.body.password) {
        req.body.password = CryptoJS.AES.encrypt(
            req.body.password,
            process.env.PASS_SEC
        ).toString();
    }

    try {
        const updatedUser = await User.findByIdAndUpdate(req.params.id, {
            $set: req.body},
            {new: true}
        );
        res.status(200).json(updatedUser);
    } catch (err) {
        res.status(500).json("Error" + err);
    }
});

//DELETE
router.delete("/:id", verifyTokenAndAuthorization, async(req, res) => {
    try {
        await User.findByIdAndDelete(req.params.id);
        res.status(200).json("User has been deleted...");
    } catch (err) {
        res.status(500).json(err);
    }
});

//GET USER
router.get("/find/:id", verifyTokenAndAdmin, async(req, res) => {
    try {
        const user = await User.findById(req.params.id);
        const { password, ...others } = user._doc;
        console.log("\n\nUser DOC : " + user._doc);
        res.status(200).json(others);
    } catch (err) {
        res.status(500).json(err);
    }
});

//GET ALL USERS
router.get("/", verifyTokenAndAdmin, async(req, res) => {
    const query = req.query.new;
    try {
        const users = query ? await User.find().sort({_id: -1}).limit(5) : await User.find();             //find all users  //if new is true select recent 5 users alone
        res.status(200).json(users);
    } catch (err) {
        res.status(500).json(err);
    }
});


//??? NOT WORKING
//GET USER STATS
//display number of user accounts for each month
router.get("/stats", verifyTokenAndAdmin, async(req, res) => {
    const date = new Date();
    const lastYear = new Date(date.setFullYear(date.getFullYear() - 1));

    try {
        const data = await User.aggregate([
        {$match: {createdAt: {$gte: lastYear}}},
        {
            $project: {
                month: {$month: "$createdAt"},           //get month alone from createdAt
            },
        },
        {
            $group: {
                _id: "$month",
                total: { $sum: 1 },     //add every registered user
            },
        },
        ]);
        res.status(200).json(data);
    } catch(err) {
        res.status(500).json(err);
    }
});

module.exports = router;