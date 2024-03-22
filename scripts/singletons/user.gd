extends Node

@onready var card_utils = get_node("/root/CardUtils")

var id : String
var username : String
var email : String
var gold : int
var crystals : int
var deck : Array
var collection : Array

func start_user(dict : Dictionary):
	id = dict.id
	username = dict.name
	email = dict.email
	gold = dict.gold
	crystals = dict.crystals
	var collection = []
	for card in dict.collection:
		collection.append(card_utils.convert_dictionary_to_card(card))
	collection = collection
	var deck = []
	for card in dict.deck:
		deck.append(card_utils.convert_dictionary_to_card(card))
	deck = deck
