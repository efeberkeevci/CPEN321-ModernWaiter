const express = require('express')
const mysql = require('mysql')
const push_notification = require("./../push_notification.js")
const { subscribe, messageAccountisClosed } = require("./../push_notification.js")
const env = require("dotenv").config({ path: "./.env" })
const stripe = require("stripe")(process.env.STRIPE_SECRET_KEY)

module.exports = function(app){
    app.use(express.json());
    
    /**
     * HTTP GET request to get the stripe
     * key required to pay for an order.
     * Returns 200 if successful.
     */
    app.get('/key', (req, res) => {
        res.send({ publishableKey: process.env.STRIPE_PUBLISHABLE_KEY })
    })
    
    /**
     * HTTP POST request to make a
     * payment. Returns 200 if successful.
     */
    app.post('/pay', async(req, res) => {
    const { paymentMethodId, paymentIntentId, currency, useStripeSdk, orderAmount } = req.body

    try {
        let intent
        if (paymentMethodId) {
            
        // Create new PaymentIntent with a PaymentMethod ID from the client.
        intent = await stripe.paymentIntents.create({
            amount: orderAmount,
            currency: currency,
            payment_method: paymentMethodId,
            confirmation_method: "manual",
            confirm: true,
            // If a mobile client passes `useStripeSdk`, set `use_stripe_sdk=true`
            // to take advantage of new authentication features in mobile SDKs
            use_stripe_sdk: useStripeSdk,
        })
        // After create, if the PaymentIntent's status is succeeded, fulfill the order.
        } else if (paymentIntentId) {
        // Confirm the PaymentIntent to finalize payment after handling a required action
        // on the client.
        intent = await stripe.paymentIntents.confirm(paymentIntentId)
        // After confirm, if the PaymentIntent's status is succeeded, fulfill the order.
        }
        res.send(generateResponse(intent))
    } catch (e) {
            // Handle "hard declines" e.g. insufficient funds, expired card, etc
            // See https://stripe.com/docs/declines/codes for more
            res.send({ error: e.message })
        }
    })

    const generateResponse = intent => {
        // Generate a response based on the intent's status
        switch (intent.status) {
            case "requires_action":
            case "requires_source_action":
            // Card requires authentication
            return {
                requiresAction: true,
                clientSecret: intent.client_secret
            }
            case "requires_payment_method":
            case "requires_source":
            // Card was not properly authenticated, suggest a new payment method
            return {
                error: "Your card was denied, please provide a new payment method"
            }
            case "succeeded":
            // Payment is complete, authentication not required
            // To cancel the payment after capture you will need to issue a Refund (https://stripe.com/docs/api/refunds)
            return { clientSecret: intent.client_secret }
        }
    }
}
