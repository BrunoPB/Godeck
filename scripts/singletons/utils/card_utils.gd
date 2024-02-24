extends Node

func convert_dictionary_to_card(dictionary : Dictionary) -> Card:
	var card : Card = Card.new()
	card.id = dictionary.id
	card.number = dictionary.number
	card.character_name = dictionary.name
	card.tier = dictionary.tier
	card.mythology = dictionary.mythology
	card.file_name = dictionary.fileName
	card.price = dictionary.price
	card.stars = dictionary.stars
	card.north = dictionary.north
	card.north_east = dictionary.northEast
	card.south_east = dictionary.southEast
	card.south = dictionary.south
	card.south_west = dictionary.southWest
	card.north_west = dictionary.northWest
	return card
