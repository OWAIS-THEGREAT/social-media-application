# Generated by Django 4.2.2 on 2023-08-23 13:34

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Profile', '0003_post'),
    ]

    operations = [
        migrations.CreateModel(
            name='like_post',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('post_id', models.CharField(max_length=500)),
                ('username', models.CharField(max_length=50)),
            ],
        ),
    ]
