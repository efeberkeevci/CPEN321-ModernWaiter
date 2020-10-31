const express = require('express')
const router = express.Router

router.get('/test', (req, res) => {
    console.log("Test here")
    res.end()
})

module.exports = router