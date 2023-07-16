const router = require("express").Router();
const User = require("../models/User");
const CryptoJS = require("crypto-js");
const jwt = require("jsonwebtoken");

/** REGISTER */
router.post("/register", async (req, res) => {
    const newUser = new User({
        username: req.body.username,
        email: req.body.email,
        password: CryptoJS.AES.encrypt(req.body.password, process.env.PASS_SEC).toString(),
    });

    console.log(req.body.username);

    try{
    const savedUser = await newUser.save();
    res.status(201).json(savedUser);    //status 201 - successfully added
    //it will take sometime to save it to the database, so use async function
    console.log(savedUser);
    } catch (err) {
        res.status(500).json(err);
        console.log(err);
    }
})

//LOGIN

router.post("/login", async(req, res) => {
    try{
        const user = await User.findOne({username: req.body.username});
        !user && res.status(401).json("Wrong username");
        const hashPwd = CryptoJS.AES.decrypt(user.password, process.env.PASS_SEC);
        const origpassword = hashPwd.toString(CryptoJS.enc.Utf8);
        console.log("\n\n\n"+req.body.password+"\n\n\n");

        const ipPwd = req.body.password;
        origpassword !=  ipPwd && res.status(401).json("Wrong credentials");

        const accessToken = jwt.sign({
            id: user._id,
            isAdmin: user.isAdmin,
        },
        process.env.JWT_SEC,
        {expiresIn: "3d"}
        );

        const { password, ...others} = user._doc;

        res.status(200).json({...others, accessToken});

    } catch (err) {
        res.status(500).json(err);
    }
});

module.exports = router;