from django.db import models
from django.contrib.auth import get_user_model
import uuid
from datetime import datetime

# Create your models here.

User = get_user_model()

class profile(models.Model):
    user = models.ForeignKey(User,on_delete=models.CASCADE)
    id_user = models.IntegerField()
    bio = models.TextField(blank=True)
    location = models.CharField(max_length=50,blank=True)
    profileimage = models.ImageField(upload_to='images',null=True)
    
    
    def __str__(self):
        return self.user.username
    

class post(models.Model):
    id = models.UUIDField(primary_key=True,default=uuid.uuid4)
    user = models.CharField(max_length=50)
    image = models.ImageField(upload_to='posts/')
    created_at = models.DateTimeField(default=datetime.now)
    caption = models.TextField()
    no_of_likes = models.IntegerField(default=0)
    
    def __str__(self):
        return self.user
    
class like_post(models.Model):
    post_id = models.CharField(max_length=500)
    username = models.CharField(max_length=50)
    
    def __str__(self):
        return self.username
    
class follower_count(models.Model):
    follower = models.CharField(max_length=50)
    user = models.CharField(max_length=50)
    
    def __str__(self):
        return self.user
    
class chatbot(models.Model):
    user = models.ForeignKey(User,on_delete=models.CASCADE)
    sender = models.CharField(max_length=50)
    botres = models.CharField(max_length=50)
    
    def __str__(self):
        return self.user.username