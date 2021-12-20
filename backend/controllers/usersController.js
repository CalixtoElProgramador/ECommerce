const User = require('../models/user');

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
            return res.status(200).json({
                success: true,
                message: 'The register was realized successfully',
                data: data.id
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
    }
    
};