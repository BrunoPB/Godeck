extends Node

class_name Card

var id : String
var number : int
var card_name : String
var tier : int
var mythology : int
var file_name : String
var price: int
var stars: int
var north: int
var north_east: int
var south_east: int
var south: int
var south_west: int
var north_west: int


func _init(dictionary : Dictionary = {}):
	if dictionary != {} :
		id = dictionary.id
		number = dictionary.number
		card_name = dictionary.cardName
		tier = dictionary.tier
		mythology = dictionary.mythology
		file_name = dictionary.fileName
		price = dictionary.price
		stars = dictionary.stars
		north = dictionary.north
		north_east = dictionary.northEast
		south_east = dictionary.southEast
		south = dictionary.south
		south_west = dictionary.southWest
		north_west = dictionary.northWest

func toJSONString():
	return "{\"id\":\"" + id + "\",\"number\":" + str(number) + ",\"cardName\":\"" + card_name + "\",\"tier\":" + str(tier) + ",\"mythology\":" + str(mythology) + ",\"fileName\":\"" + file_name + "\",\"price\":" + str(price) + ",\"stars\":" + str(stars) + ",\"north\":" + str(north) + ",\"northEast\":" + str(north_east) + ",\"southEast\":" + str(south_east) + ",\"south\":" + str(south) + ",\"southWest\":" + str(south_west) + ",\"northWest\":" + str(north_west) + "}"
