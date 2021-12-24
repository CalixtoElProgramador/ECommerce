const Product = require('../models/product');
const storage = require('../utils/cloud_storage');
const asyncForEach = require('../utils/async_foreach');

module.exports = {

    async findByCategory(req, res, next) {
        try {
            const id_category = req.params.id_category;
            const data = await Product.findByCategory(id_category);
            return res.status(201).json({
                success: true,
                message: `The quantity of products with category ${id_category} is: ${data.length}`,
                data: data
            });
        } catch (error) {
            console.log(`Error: ${error}`);
                res.status(501).json({
                    success: false,
                    message: `Error when registering the product. ${error}`,
                    error: error
                });
        }
    },

    async create(req, res, next) {

        let product = JSON.parse(req.body.product);

        const files = req.files

        let inserts = 0;

        if (files.length === 0) {
            return res.status(501).json({
                success: false,
                message: 'At least one image needed to save a product'
            });
        } else {
            try {

                const data = await Product.create(product) // Storage product data
                product.id = data.id;
                
                const start = async () => {
                    await asyncForEach(files, async (file) => {
                        const pathImage = `image_${Date.now()}`
                        const url = await storage(file, pathImage)

                        if (url !== undefined && url !== null) {
                            if (inserts === 0) {
                                product.image00 = url;
                            } else if (inserts === 1) {
                                product.image01 = url;
                            } else if (inserts === 2) {
                                product.image02 = url;
                            }
                        }
                        await Product.update(product);
                        inserts = inserts + 1;

                        if (inserts == files.length) {
                            return res.status(201).json({
                                success: true,
                                message: 'The product was registered successfully'
                            });
                        }

                    });
                }

                start();

            } catch(error) {
                console.log(`Error: ${error}`);
                res.status(501).json({
                    success: false,
                    message: `Error when registering the product. ${error}`,
                    error: error
                });
            }
        }

    }

}
