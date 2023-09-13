from django.contrib import admin
from .models import profile,post,like_post,follower_count,chatbot
# Register your models here.

admin.site.register(profile)
admin.site.register(post)
admin.site.register(like_post)
admin.site.register(follower_count)
admin.site.register(chatbot)