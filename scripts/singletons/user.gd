extends Node

var id : String
var username : String
var display_name : String
var email : String
var gold : int
var crystals : int
var deck : Array
var collection : Array
var ghost : bool
var friends : Array

func start_user(dict : Dictionary):
	id = dict.id
	username = dict.username
	display_name = dict.displayName
	ghost = dict.ghost
	if not ghost:
		email = dict.email
	gold = dict.gold
	crystals = dict.crystals
	for card in dict.collection:
		collection.append(Card_Utils.convert_dictionary_to_card(card))
	for card in dict.deck:
		deck.append(Card_Utils.convert_dictionary_to_card(card))
	for friend in dict.friends:
		friends.append(Friend.new(friend.id, friend.username, friend.display_name, friend.deck, friend.collection))
