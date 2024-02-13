extends Node2D

@onready var paths = get_node("/root/Constants").card_base_paths
var card_data : Card = Card.new()
var radius : int = 40
var panel_position : Vector2 = Vector2(0,0)

func _ready():
	pass

func update_all():
	update_panel_position()
	update_size()
	update_texture()
	update_stats()

func update_panel_position():
	get_node(paths["panel"]).position = panel_position

func update_size():
	pass

func update_texture():
	pass

func update_stats():
	get_node(paths["stats"] + "/North").text = str(card_data.north)
	get_node(paths["stats"] + "/NorthSide/NorthWest").text = str(card_data.north_west)
	get_node(paths["stats"] + "/NorthSide/NorthEast").text = str(card_data.north_east)
	get_node(paths["stats"] + "/SouthSide/SouthWest").text = str(card_data.south_west)
	get_node(paths["stats"] + "/SouthSide/SouthEast").text = str(card_data.south_east)
	get_node(paths["stats"] + "/South").text = str(card_data.south)
