const User = require('../models/user');
const Role = require('../models/role');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const keys = require('../config/keys');

module.exports = {

    async getAll(req, res, next) {
        try {
            const data = await User.getAll();
            console.log(`Usuarios: ${data}`);
            return res.status(201).json(data);
        } 
        catch (error) {
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'Error to obtain users'
            });
        }
    },

    async register(req, res, next) {
        try {
            const user = req.body;
            const data = await User.create(user);

            await Role.create(data.id, 1);
            const token = jwt.sign({ id: data.id, email: user.email }, keys.secretOrKey, {
                // expiresIn: 
            })

            const myData = {
                id: data.id,
                name: user.name,
                lastname: user.lastname,
                email: user.email,
                phone: user.phone,
                image: user.image,
                session_token: `JWT ${token}`
            };

            return res.status(200).json({
                success: true,
                message: 'The register was realized successfully',
                data: myData
            });
        }
        catch (error) {
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'Error to register the user',
                error: error
            });
            
        }
    }, 

    async login(req, res, next) {
        try {
            const email = req.body.email;
            const password = req.body.password;
            
            const myUser = await User.findByEmail(email)
            if (!myUser) {
                return res.status(401).json({
                    success: false,
                    message: 'The email was not founded'
                });
            }

            const isPasswordValid = await bcrypt.compare(password, myUser.password);
            
            if (isPasswordValid) {
                const token = jwt.sign({ id: myUser.id, email: myUser.email }, keys.secretOrKey, {
                    // expiresIn: 
                })

                const data = {
                    id: myUser.id,
                    name: myUser.name,
                    lastname: myUser.lastname,
                    email: myUser.email,
                    phone: myUser.phone,
                    image: myUser.image,
                    session_token: `JWT ${token}`,
                    roles: myUser.roles
                };

                return res.status(201).json({
                    success: true,
                    message: 'The user was authenticated',
                    data: data
                });

            }

            else {
                return res.status(401).json({
                    success: false,
                    message: 'The password is incorrect'
                });
            }

        } 
        catch (error) {
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'Error to login the user',
                error: error
            });
        }
    }
    
};