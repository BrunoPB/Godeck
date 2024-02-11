extends PanelContainer

@onready var card_utils = get_node("/root/CardUtils")
@onready var paths = get_node("/root/Constants").deck_builder_paths
@onready var user = get_node("/root/User")
@onready var deck_edit = preload("res://scenes/deck_edit.tscn")

func _ready():
	build_deck_gui()
	build_collection_gui()

func build_deck_gui():
	## New way of displaying hex grid
	# Create instance of deck
	var deck_instance = deck_edit.instantiate()
	# Add it to the tree
	$VerticalLayout/CurrentDeck.add_child(deck_instance)
	
	## Old way of displaying hex grid
	#const OFFSET = Vector2i(40,0)
	#const SEPARATION = 1
	#const HEX_RADIUS = 40
	#var hex_slot = HEX_RADIUS + SEPARATION
	#var hex_height : int = sqrt(3) * hex_slot
	#var coords = [
				#Vector2i(OFFSET.x + hex_slot*1.5,OFFSET.y),
		#Vector2i(OFFSET.x,OFFSET.y + hex_height/2),	Vector2i(OFFSET.x + 3*hex_slot,OFFSET.y + hex_height/2),
				#Vector2i(OFFSET.x + hex_slot*1.5,OFFSET.y + hex_height),
		#Vector2i(OFFSET.x,OFFSET.y + 1.5*hex_height),	Vector2i(OFFSET.x + 3*hex_slot,OFFSET.y + 1.5*hex_height),
				#Vector2i(OFFSET.x + hex_slot*1.5,OFFSET.y + 2*hex_height)
		#]
	#for i in range(7):
		#var hex = card_utils.create_card_gui(coords[i],HEX_RADIUS,user.deck[i])
		#get_node(paths["deck_area"]).add_child(hex)

func build_collection_gui():
	for card in user.collection:
		var hex_panel = card_utils.create_card_panel_container(Vector2i(0,0),40,card)
		get_node(paths["collection_grid"]).add_child(hex_panel)
 
