const jwt = require("jsonwebtoken");


//middleware to verify the token

const verifyToken = (req, res, next) => {
    const authHeader = req.headers.token;
    console.log(authHeader);
    if(authHeader) {
        const token = authHeader.split(" ")[1];         //format : Bearer + token -> so extract token alone
        jwt.verify(token, process.env.JWT_SEC, (err, user) => {
            if(err) res.status(403).json("Token not valid");            //expired or wrong token
            req.user = user;
            console.log("\n User Data : " + req.user + "\n\n");
            next();
        })
    } else {
        return res.status(401).json("You are not authenticated!");
    }
};

const verifyTokenAndAuthorization = (req, res, next) => {
    verifyToken(req, res, () => {
        console.log(req.user.id);
        //console.log(req.params.userId);
        if(req.user.id === req.params.id || req.user.isAdmin) {
            console.log("\nPARAMS ID : " + req.params.id + "\n\n");
            next();
        } else {
            res.status(403).json("You are not allowed to do that");
        }
    });
};

const verifyTokenAndAuthorization2 = (req, res, next) => {
    verifyToken(req, res, () => {
        console.log(req.user.id);
        console.log(req.params.userId);
        if(req.user.id === req.params.userId || req.user.isAdmin) {
            console.log("\nPARAMS ID : " + req.params.userId + "\n\n");
            next();
        } else {
            res.status(403).json("You are not allowed to do that");
        }
    });
};

const verifyTokenAndAdmin = (req, res, next) => {
    verifyToken(req, res, () => {
        if(req.user.isAdmin) {
            next();
        } else {
            res.status(403).json("You are not allowed to do that");
        }
    });
};

module.exports = { verifyToken, verifyTokenAndAuthorization, verifyTokenAndAdmin, verifyTokenAndAuthorization2 };