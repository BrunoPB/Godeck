extends Node

class_name InGameCard

var card_owner : int
var current_dominator : int
var card : Card
var exists : bool

func _init(dictionary : Dictionary = {}):
	if dictionary != {} and dictionary.exists:
		card_owner = dictionary.cardOwner
		current_dominator = dictionary.currentDominator
		card = Card.new(dictionary.card)
		exists = true
	else:
		exists = false

func toJSONString() -> String:
	return "{\"cardOwner\": " + str(card_owner) + ", \"currentDominator\": " + str(current_dominator) + ", \"card\": " + card.toJSONString() + ", \"exists\": " + str(exists) + "}"
