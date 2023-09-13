from django.shortcuts import render
from django.http import HttpResponse
from django.contrib.auth.models import User
from .models import profile,post,like_post,follower_count,chatbot
from django.contrib import auth
from rest_framework.response import Response
from rest_framework.decorators import api_view,permission_classes,authentication_classes
from rest_framework.permissions import AllowAny,IsAuthenticated
from rest_framework.authentication import TokenAuthentication
from rest_framework.authtoken.models import Token
from rest_framework.parsers import MultiPartParser
from rest_framework.decorators import parser_classes
from django.core.files.base import ContentFile
from base64 import b64encode
import os
from django.core.files import File

# Create your views here.
@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def home(request):
    return HttpResponse("hello world")

@api_view(['POST'])
@permission_classes([AllowAny])
def signup(request):
    
    if request.method=='POST':
        username = request.POST['username']
        email = request.POST['email']
        pass1 = request.POST['pass1']
        pass2 = request.POST['pass2']
        
        if pass1==pass2:
            if User.objects.filter(email=email).exists():
                return Response({'error':"email exists"})
            
            elif User.objects.filter(username=username).exists():
                return Response({'error':"username exists"})
            
            else:
                user = User.objects.create_user(username=username,email=email,password=pass1)
                user.save()
                token, created = Token.objects.get_or_create(user=user)
                
                
                user_model = User.objects.get(username=username)
                Profile = profile.objects.create(user = user_model,id_user = user_model.id)
                Profile.save()
                userdata = {
                    'username':user_model.username,
                    'token': token.key
                }
                return Response(userdata)
        else:
            return Response({'error':"password not matched"}) 

@api_view(['POST'])
@permission_classes([AllowAny])
def signin(request):
    if request.method=='POST':
        username = request.POST['username']
        password = request.POST['password']
        
        user =  auth.authenticate(username=username,password=password)
        
        if user is not None:
            auth.login(request,user)   
            return Response({'message':"OK"})      
        else:
            return Response({'error':'username/password doesnot match'})     
        
        
def logout(request):
    auth.logout(request)
    return Response({'message':'OK'})

@api_view(['POST'])
# @authentication_classes([TokenAuthentication])
@permission_classes([AllowAny])
# @parser_classes([MultiPartParser])
def setting(request):
    # print(request.data)
    if request.user.is_authenticated:
        profile_user = profile.objects.get(user=request.user)

        if request.method == 'POST':
            profileimage = request.FILES['image']  
            location = request.data.get('location')
            bio = request.data.get('bio')
            
            if profileimage:
                profile_user.bio = bio
                profile_user.location = location
                
                image_url = request.build_absolute_uri(profile_user.profileimage.url)
                profile_user.profileimage.save(str(profile_user.id_user)+".jpg",profileimage)
                
                profile_user.save()
                userdata = {
                    'username': profile_user.id_user,
                    'bio': profile_user.bio,
                    'image': image_url
                }

                return Response(userdata)
            else:
                profile_user.bio = bio
                profile_user.location = location

                profile_user.save()
                userdata = {
                    'username': profile_user.id_user,
                    'bio': profile_user.bio,
                    'image': None  # Set to None or another appropriate value for no image
                }

                return Response(userdata)
    else:
        return Response({"err":"not authenticated"})
        
@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def upload(request):
    if request.method=='POST':
        user = request.user.username
        caption = request.POST['caption']
        image = request.FILES['image']
        if image:
            Post = post.objects.create(user = user,caption = caption,image = image)
            image_url = request.build_absolute_uri(Post.image.url)
            # Post.image.save(str(Post.id)+".jpg",image)
            
            Post.save()
            userdata = {
                'username':user
            }
            return Response(userdata)
        else:
            return Response({'error':'image not found'})
        

@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def getposts(request):
    user_obj = request.user
    # profile_obj = profile.objects.get(user=user_obj)

    posts = post.objects.all()

    serialized_entries = []

    for entry in posts:
        # Get the absolute URL of the post's image if it exists
        image_url = None
        if entry.image:
            image_url = request.build_absolute_uri(entry.image.url)
            

        # Serialize the entry data
        serialized_entry = {
            'caption': entry.caption,
            'user': entry.user,
            'post_image_url': image_url,
            'id':entry.id,
            'likes':entry.no_of_likes
        }

        serialized_entries.append(serialized_entry)

    return Response(serialized_entries)



@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def likepost(request):
    username = request.user.username
    # profile_obj = profile.objects.get(user_obj)
    
    post_id = request.POST['id']
    
    posts = post.objects.get(id=post_id)
    
    likefilter = like_post.objects.filter(post_id=post_id,username=username).first()
    
    if likefilter==None:
        
        new_like = like_post.objects.create(post_id=post_id,username=username)
        new_like.save()
        posts.no_of_likes = posts.no_of_likes+1
        
        posts.save()
    else:
        likefilter.delete()
        posts.no_of_likes = posts.no_of_likes-1
        
        posts.save()  
    
    return Response({
        'no_of_likes':posts.no_of_likes
        }) 
        
@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def follower(request):
    if request.method == 'POST':
        follower = request.POST['follower']
        user =request.POST['user']
        
        if follower_count.objects.filter(follower=follower,user=user).first()==None:
            new_follower = follower_count.objects.create(follower=follower,user=user)
            new_follower.save()
        
        else:
            new_follower = follower_count.objects.get(follower=follower,user=user)
            new_follower.delete()
            
        return Response({'message':'Ok'})

@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def checkfoll(request):
    if request.method == "POST":
        follower = request.POST['follower']
        user =request.POST['user']
        
        if follower_count.objects.filter(follower=follower,user=user).first()==None:
            return Response({
                "message":"yes"
            })
        
        else:
            
            return Response({
                "message":"no"
            })
            
            
@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def checklike(request):
    username = request.user.username
    # profile_obj = profile.objects.get(user_obj)
    
    post_id = request.POST['id']
    
    posts = post.objects.get(id=post_id)
    
    likefilter = like_post.objects.filter(post_id=post_id,username=username).first()
    
    if likefilter==None:
        return Response({
            "message":"yes"
        })
    else:
        return Response({
            "message":"no"
        })


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def chatting(request):
    if request.method=="POST":
        user = request.user
        
        sender = request.POST['sender']
        botres = request.POST['chatbot']
        
        bot = chatbot.objects.create(user=user,sender=sender,botres=botres)
        bot.save()
        entries = chatbot.objects.filter(user=user)
        serialized_entries = [{'sender': entry.sender,"botres":entry.botres} for entry in entries]
        
        return Response(serialized_entries)
    


@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def getchatting(request):
    if request.method=="POST":
        user = request.user
        
        entries = chatbot.objects.filter(user=user)
        serialized_entries = [{'sender': entry.sender,"botres":entry.botres} for entry in entries]
        
        return Response(serialized_entries)
        
        
        
    
        

    
            
