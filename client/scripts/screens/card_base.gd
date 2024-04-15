extends Node2D

@onready var s_panel : Node = %Layout
@onready var s_hex : Node = %CardHex
@onready var s_border : Node = %Border

var card_data : Card = Card.new()
var in_board : bool = false
var dominated : bool
var selected : bool = false
var radius : int = 40
var panel_position : Vector2 = Vector2(0,0)
var exists : bool = true

func _ready():
	pass

func update_all():
	update_panel_position()
	update_size()
	update_texture()
	update_border()
	update_stats()

func update_panel_position():
	s_panel.position = panel_position

func update_size():
	pass

func update_texture():
	var tex = Texture2D.new()
	if exists:
		var lower_case_name = card_data.card_name.to_lower()
		s_hex.texture = load(Address.CHARACTERS_IMGS + lower_case_name + ".png")
	else:
		s_hex.texture = tex.create_placeholder()

func update_border():
	var color : Color
	if not exists:
		color = Color.BLACK
	elif selected:
		color = Color.WHITE_SMOKE
	elif in_board and dominated:
		color = Color.WEB_GREEN
	elif in_board and not dominated:
		color = Color.DARK_RED
	s_border.default_color = color

func update_stats():
	%North.text = str(card_data.north) if exists else ""
	%NorthWest.text = str(card_data.north_west) if exists else ""
	%NorthEast.text = str(card_data.north_east) if exists else ""
	%SouthWest.text = str(card_data.south_west) if exists else ""
	%SouthEast.text = str(card_data.south_east) if exists else ""
	%South.text = str(card_data.south) if exists else ""

func set_selection(v : bool = true):
	selected = v
	z_index = 99 if v else 0
