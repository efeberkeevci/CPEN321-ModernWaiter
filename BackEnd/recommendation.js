function jensenShannonDivergenceCalculator(sampleA, sampleB){
    var preferenceMap = new Map()
    var descriptionMap = new Map()
    var allWords = []
  
    sampleA.forEach(item => {
      if(preferenceMap.get(item) != null){
        preferenceMap.set(item, preferenceMap.get(item) + 1)
      }
      else {
        preferenceMap.set(item, 1)
      }
  
      if(allWords[item] == null){
        allWords.push(item)
      }
    })
  
    sampleB.forEach(item => {
      if(descriptionMap.get(item) != null){
        descriptionMap.set(item, descriptionMap.get(item) + 1)
      }
      else {
        descriptionMap.set(item, 1)
      }
  
        if(allWords[item] == null){
        allWords.push(item)
      }
    })
  
    var uniqueWordSet = [...new Set(allWords)]
  
    var JSD = 0
    var Pcount = preferenceMap.size
    var Qcount = descriptionMap.size
  
    var score = 0
    var p;
    var q;
    var m;
  
    var plogp_m
    var qlogq_m
  
    allWords.forEach(item => {
      if(preferenceMap.get(item) != null){
        p = preferenceMap.get(item) / Pcount
      } else {
        p = 0
      }
  
      if(descriptionMap.get(item) != null){
        q = descriptionMap.get(item) / Qcount
      } else {
        q = 0
      }
  
      m = (p + q)/2
  
      if(p != 0){
        plogp_m = p * (Math.log(p/m)/Math.log(2))
      } else {
        plogp_m = 0
      }
  
      if(q != 0){
        qlogq_m = q * (Math.log(q/m)/Math.log(2))
      } else {
        qlogq_m = 0
      }
  
      score += plogp_m + qlogq_m
    })
  
    JSD = 50 * score
  
    return JSD
  }
  
  function getRecommendation(preference, itemDescriptionMap){    
    var itemScoreMap = new Map()
    var itemKeys = Array.from(itemDescriptionMap.keys());
    var preferenceArray = normalizeStringToArray(preference)

    itemKeys.forEach(itemId => {
        var descriptionArray = normalizeStringToArray(itemDescriptionMap.get(itemId))
	var jsdScore = jensenShannonDivergenceCalculator(preferenceArray, descriptionArray)
        itemScoreMap.set(jsdScore, itemId)
    })
    
    console.log(itemScoreMap)
    var itemScoreMapKeys = Array.from(itemScoreMap.keys());
    itemScoreMapKeys = itemScoreMapKeys.sort(function(a, b){return a-b})
    console.log(itemScoreMapKeys)
  
    return itemScoreMap.get(itemScoreMapKeys[0])
  }

  function normalizeStringToArray(stringInput){
      return stringInput
      .replace(/[^\w\s]|_/g, "")
      .replace(/\s+/g, " ")
      .toLowerCase()
      .split(" "); 
  }
  
 module.exports = {getRecommendation};
