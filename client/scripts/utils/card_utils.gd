extends Node

class_name Card_Utils

static func convert_dictionary_to_in_game_card(dictionary : Dictionary) -> InGameCard:
	var in_game_card = InGameCard.new()
	in_game_card.card_owner = dictionary.cardOwner
	in_game_card.current_dominator = dictionary.cardDominator
	in_game_card.card = convert_dictionary_to_card(dictionary.card)
	return in_game_card

static func convert_dictionary_to_card(dictionary : Dictionary) -> Card:
	var card : Card = Card.new()
	card.id = dictionary.id
	card.number = dictionary.number
	card.card_name = dictionary.cardName
	card.tier = dictionary.tier
	card.mythology = dictionary.mythology
	card.price = dictionary.price
	card.stars = dictionary.stars
	card.north = dictionary.north
	card.north_east = dictionary.northEast
	card.south_east = dictionary.southEast
	card.south = dictionary.south
	card.south_west = dictionary.southWest
	card.north_west = dictionary.northWest
	return card
