extends Node

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
	for card in dict.collection:
		collection.append(Card_Utils.convert_dictionary_to_card(card))
	for card in dict.deck:
		deck.append(Card_Utils.convert_dictionary_to_card(card))
