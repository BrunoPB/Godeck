extends Node

func create_card_gui(coords : Vector2i, radius: int, card_data: Card) -> Node2D:
	var node = Node2D.new()
	node.name = "Card"
	node.position = coords
	node.add_child(create_card_panel_container(Vector2i(0,0), radius, card_data))
	return node

func create_card_panel_container(coords : Vector2i, radius, card_data: Card) -> PanelContainer:
	var panel = PanelContainer.new()
	panel.size = Vector2i(radius*2,sqrt(3) * radius)
	panel.custom_minimum_size = Vector2i(radius*2,sqrt(3) * radius)
	panel.theme = load("res://themes/card_panel_theme.tres")
	panel.add_child(create_vertical_hexagon_polygon(coords, radius, card_data))
	panel.add_child(create_card_stats_gui(coords, radius, card_data))
	return panel

func create_card_stats_gui(coords: Vector2i, radius: int, card_data: Card) -> VBoxContainer:
	var label = Label.new()
	label.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	
	var north_label = label.duplicate()
	north_label.text = str(card_data.north)
	north_label.name = "North"
	var north_east_label = label.duplicate()
	north_east_label.text = str(card_data.north_east)
	north_east_label.name = "NorthEast"
	var south_east_label = label.duplicate()
	south_east_label.text = str(card_data.south_east)
	south_east_label.name = "SouthEast"
	var south_label = label.duplicate()
	south_label.text = str(card_data.south)
	south_label.name = "South"
	var south_west_label = label.duplicate()
	south_west_label.text = str(card_data.south_west)
	south_west_label.name = "SouthWest"
	var north_west_label = label.duplicate()
	north_west_label.text = str(card_data.north_west)
	north_west_label.name = "NorthWest"
	
	var hbox = HBoxContainer.new()
	hbox.add_theme_constant_override("separation", 35)
	hbox.alignment = BoxContainer.ALIGNMENT_CENTER
	hbox.custom_minimum_size = Vector2(coords.x,coords.y + 20)
	var north_side_box = hbox.duplicate()
	north_side_box.name = "NorthSide"
	var south_side_box = hbox.duplicate()
	south_side_box.name = "SouthSide"
	
	north_side_box.add_child(north_west_label)
	north_side_box.add_child(north_east_label)
	south_side_box.add_child(south_west_label)
	south_side_box.add_child(south_east_label)
	
	var stats_box = VBoxContainer.new()
	stats_box.name = "Stats"
	stats_box.size_flags_horizontal = Control.SIZE_FILL
	stats_box.size_flags_vertical = Control.SIZE_FILL
	stats_box.theme = load("res://themes/deck_builder_themes/card_numbers_theme.tres")
	stats_box.add_theme_constant_override("separation", 0)
	stats_box.add_child(north_label)
	stats_box.add_child(north_side_box)
	stats_box.add_child(south_side_box)
	stats_box.add_child(south_label)
	
	return stats_box

func create_vertical_hexagon_polygon(coords : Vector2i, radius, card_data : Card) -> Polygon2D:
	var hex = Polygon2D.new()
	hex.polygon = create_vertical_hexagon_vector(coords, radius)
	hex.color = Color(0,0,0,1)
	hex.add_child(create_vertical_hexagon_collision_area(coords, radius))
	return hex

func create_vertical_hexagon_collision_area(coords : Vector2i, radius) -> Area2D:
	var hex = CollisionPolygon2D.new()
	hex.polygon = create_vertical_hexagon_vector(coords, radius)
	var area = Area2D.new()
	area.add_child(hex)
	return area

func create_vertical_hexagon_vector(coords : Vector2i, radius) -> PackedVector2Array:
	var x1 = coords.x
	var x2 = coords.x + (radius/2)
	var x3 = coords.x + (radius*3/2)
	var x4 = coords.x + radius*2
	var y1 = coords.y
	var y2 = coords.y + (sqrt(3) * radius)/2
	var y3 = coords.y + (sqrt(3) * radius)
	var p0 = Vector2i(x2,y1)
	var p1 = Vector2i(x3,y1)
	var p2 = Vector2i(x4,y2)
	var p3 = Vector2i(x3,y3)
	var p4 = Vector2i(x2,y3)
	var p5 = Vector2i(x1,y2)
	return PackedVector2Array([p0,p1,p2,p3,p4,p5])
