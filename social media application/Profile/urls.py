from django.urls import path,include
from . import views
urlpatterns = [
    path('home/',views.home,name='home'),
    path('signup/',views.signup,name='signup'),
    path('logout/',views.logout,name='logout'),
    path('signin/',views.signin,name='signin'),
    path('setting/',views.setting,name = 'setting'),
    path('upload/',views.upload,name = 'upload'),
    path('feed/',views.getposts,name='feed'),
    path('like/',views.likepost,name = 'like'),
    path('follow/',views.follower,name='follow'),
    path("check/",views.checkfoll,name='check'),
    path("checklike/",views.checklike,name="checklike"),
    path('chatting/',views.chatting,name="chatting"),
    path("getchats/",views.getchatting,name="getchats"),
]