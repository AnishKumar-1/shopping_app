resource "aws_ecs_cluster" "main" {
  name = "shopping-app-cluster"
  tags = {
    Name = "shopping-app-cluster"
  }
}