const pool = require('../config/db');

class Customer {
    static async create(customerData) {
        const { id, companyName, contactName, primaryPhone, primaryEmail, physicalAddress, website } = customerData;
        const query = `
            INSERT INTO customers (id, company_name, contact_name, primary_phone, primary_email, physical_address, website)
            VALUES ($1, $2, $3, $4, $5, $6, $7)
            RETURNING *
        `;
        const values = [id, companyName, contactName, primaryPhone, primaryEmail, physicalAddress, website];
        const result = await pool.query(query, values);
        return result.rows[0];
    }

    static async findAll() {
        const query = 'SELECT * FROM customers';
        const result = await pool.query(query);
        return result.rows;
    }

    static async findById(id) {
        const query = 'SELECT * FROM customers WHERE id = $1';
        const result = await pool.query(query, [id]);
        return result.rows[0];
    }

    static async update(id, customerData) {
        const { companyName, contactName, primaryPhone, primaryEmail, physicalAddress, website } = customerData;
        const query = `
            UPDATE customers
            SET company_name = $1, contact_name = $2, primary_phone = $3, 
                primary_email = $4, physical_address = $5, website = $6
            WHERE id = $7
            RETURNING *
        `;
        const values = [companyName, contactName, primaryPhone, primaryEmail, physicalAddress, website, id];
        const result = await pool.query(query, values);
        return result.rows[0];
    }

    static async delete(id) {
        const query = 'DELETE FROM customers WHERE id = $1 RETURNING *';
        const result = await pool.query(query, [id]);
        return result.rows[0];
    }
}

module.exports = Customer; 