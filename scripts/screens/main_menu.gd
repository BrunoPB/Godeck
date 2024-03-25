extends PanelContainer

@onready var home_screen_scene = preload("res://scenes/main_menu/home_screen.tscn")
@onready var deck_builder_screen_scene = preload("res://scenes/main_menu/deck_builder/deck_builder_screen.tscn")
@onready var shop_screen_scene = preload("res://scenes/main_menu/shop/shop_screen.tscn")

@onready var s_shop_button = %ShopButton
@onready var s_deck_builder_button = %DeckBuilderButton
@onready var s_home_button = %HomeButton
@onready var s_friends_button = %FriendsButton
@onready var s_events_button = %EventsButton
@onready var s_content_area = %ContentArea

func _ready():
	change_menu_screen(Menu_Items.HOME)

func _process(_delta):
	pass

func change_menu_screen(screen):
	release_menu_buttons()
	var content_area = s_content_area
	Node_Utils.remove_children(content_area)
	match screen:
		Menu_Items.HOME:
			content_area.add_child(home_screen_scene.instantiate())
			s_home_button.button_pressed = true
		Menu_Items.SHOP:
			content_area.add_child(shop_screen_scene.instantiate())
			s_shop_button.button_pressed = true
		Menu_Items.DECKBUILDER:
			content_area.add_child(deck_builder_screen_scene.instantiate())
			s_deck_builder_button.button_pressed = true
		Menu_Items.FRIENDS:
			content_area.add_child(home_screen_scene.instantiate())
			s_friends_button.button_pressed = true
		Menu_Items.EVENTS:
			content_area.add_child(home_screen_scene.instantiate())
			s_events_button.button_pressed = true

func release_menu_buttons():
	s_shop_button.button_pressed = false
	s_deck_builder_button.button_pressed = false
	s_home_button.button_pressed = false
	s_friends_button.button_pressed = false
	s_events_button.button_pressed = false

func _on_home_button_button_up():
	change_menu_screen(Menu_Items.HOME)

func _on_deck_builder_button_button_up():
	change_menu_screen(Menu_Items.DECKBUILDER)

func _on_shop_button_button_up():
	change_menu_screen(Menu_Items.SHOP)

func _on_friends_button_button_up():
	change_menu_screen(Menu_Items.FRIENDS)

func _on_events_button_button_up():
	change_menu_screen(Menu_Items.EVENTS)
