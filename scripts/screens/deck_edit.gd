extends CanvasItem

# We need user info to set up deck
@onready var user = get_node("/root/User")
# Having tilemap readily available makes setting
# it up easier
@onready var cards_tilemap = get_node("TileMap")

# Dict to make life easier (indexing of tilemap positions)
var points_dict = {0: Vector2(0,0), 1: Vector2(1,0), 
				   2: Vector2(2,0), 3: Vector2(0,1), 
				   4: Vector2(1,1), 5: Vector2(2,1), 
				   6: Vector2(1,2)
				  }

# Initial deck setup
func _ready():
	build_deck()

func build_deck():
	# Get users cards
	var user_deck = user.deck
	for i in range(7):
		# Instance a card based on user deck and points dict
		cards_tilemap.set_cell(0, points_dict.get(i), 0, Vector2i(0,0), 1)
		
		# New await is better, since it waits for the child to be added to 
		# the tree, not for an arbitrary amount of time.
		await cards_tilemap.child_entered_tree
		# Await is needed otherwise the tree doesn't have time to update
		#await get_tree().create_timer(0.00000001).timeout
		
		# Change card based on users deck
		#cards_tilemap.get_child(i).set_card(user.collection[i])

'''
--- Future TODOS ---
. The deck is not an immutable thing, it has to be able to change with user 
input and with such a need, there needs to be methods to edit deck post it's
initial setup. That means connecting signals from the UI elements to make 
the deck dynamic.
--- OBS ---
. set_cell function is a bit bizzare and works weirdly (i.e. placing tiles and
scenes in code works quite differently???), make sure to check documentation
if you plan on using/changing it (it is at least properly documented).
'''
