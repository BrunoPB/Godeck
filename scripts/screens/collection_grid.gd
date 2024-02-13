extends GridContainer

@onready var user = get_node("/root/User")
@onready var card_base = preload("res://scenes/card_base.tscn").instantiate()
var card_size : Vector2i 

func _ready():
	card_size = Vector2i(card_base.radius*2,sqrt(3)*card_base.radius)
	define_grid_columns()
	build_collection_gui()

func define_grid_columns():
	columns = floor(get_viewport_rect().size.x / card_size.x)

func build_collection_gui():
	var panel = PanelContainer.new()
	panel.custom_minimum_size = card_size
	for card_data in user.collection:
		var card = card_base.duplicate()
		card.card_data = card_data
		var dpanel = panel.duplicate()
		dpanel.add_child(card)
		add_child(dpanel)
		card.update_all()
