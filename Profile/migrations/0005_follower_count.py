# Generated by Django 4.2.2 on 2023-08-23 14:04

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Profile', '0004_like_post'),
    ]

    operations = [
        migrations.CreateModel(
            name='follower_count',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('follower', models.CharField(max_length=50)),
                ('user', models.CharField(max_length=50)),
            ],
        ),
    ]