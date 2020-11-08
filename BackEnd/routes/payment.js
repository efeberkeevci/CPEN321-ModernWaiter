const express = require('express')
const env = require("dotenv").config({ path: "./.env" })
const stripe = require("stripe")(process.env.STRIPE_SECRET_KEY)

/**
 * Gets the stripe key required to pay for
 * an order
 * @param {*} req 
 * @param {*} res Publishable key with status code 200.
 */
function getStripeKey(req, res){
    res.send({ publishableKey: process.env.STRIPE_PUBLISHABLE_KEY })
}

/**
 * Makes a payment via the Stripe API
 * @param {*} req Body includes paymentMethodId, paymentIntentId, currency, useStripeSdk, orderAmount
 * @param {*} res Status code 200 if successful, otherwise 400.
 */
async function createStripePayment(req, res){
    let paymentMethodId = req.body.paymentMethodId
    let paymentIntentId = req.body.paymentIntentId
    let currency = req.body.currency
    let useStripeSdk = req.body.useStripeSdk
    let orderAmount = req.body.orderAmount

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
}

module.exports = {getStripeKey, createStripePayment}