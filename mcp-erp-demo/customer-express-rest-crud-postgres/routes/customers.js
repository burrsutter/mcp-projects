const express = require('express');
const router = express.Router();
const Customer = require('../models/customer');

// Get all customers
router.get('/', async (req, res) => {
    try {
        const customers = await Customer.findAll();
        res.json(customers);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// Get a single customer
router.get('/:id', async (req, res) => {
    try {
        const customer = await Customer.findById(req.params.id);
        if (!customer) {
            return res.status(404).json({ message: 'Customer not found' });
        }
        res.json(customer);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// Create a new customer
router.post('/', async (req, res) => {
    try {
        const customer = await Customer.create(req.body);
        res.status(201).json(customer);
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
});

// Update a customer
router.put('/:id', async (req, res) => {
    try {
        const customer = await Customer.update(req.params.id, req.body);
        if (!customer) {
            return res.status(404).json({ message: 'Customer not found' });
        }
        res.json(customer);
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
});

// Delete a customer
router.delete('/:id', async (req, res) => {
    try {
        const customer = await Customer.delete(req.params.id);
        if (!customer) {
            return res.status(404).json({ message: 'Customer not found' });
        }
        res.json({ message: 'Customer deleted successfully' });
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

module.exports = router; 