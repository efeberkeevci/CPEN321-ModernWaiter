jest.mock('../recommendation_logic')
const recommendation = require('../recommendation_logic')

recommendation.getRecommendation.mockImplementation(() => {itemId : "1"})
console.log(recommendation.getRecommendation())