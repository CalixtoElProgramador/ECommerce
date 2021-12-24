const Category = require('../models/category');
const storage = require('../utils/cloud_storage');

module.exports = {

    async create(req, res, next) {
        try {
            const category = JSON.parse(req.body.category);
            console.log('Category', category);

            const files = req.files; 
            if (files.length > 0 /* This means the user send us a file */ ) {
                const pathImage = `image_${Date.now()}`; // File's name
                const url = await storage(files[0], pathImage);

                if (url != undefined && url != null) {
                    category.image = url; // We add in the object user the value of the image parameter
                }
            }

            const data = await Category.create(category);

            return res.status(201).json({
                success: true,
                message: 'The category was created successfully',
                body: {
                    'id': data.id
                }
            });

        } 
        catch (error) {
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'An error was happened creating a category',
                error: error
            });
        }
    },

    async getAll(req, res, next) {

        try {
            const data = await Category.getAll();
            return res.status(201).json(data);
        }
        catch (error) {
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'An error was happened creating a category',
                error: error
            });
        }

    }

}