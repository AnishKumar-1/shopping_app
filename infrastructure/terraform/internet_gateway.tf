resource "aws_internet_gateway" "shopping_igw" {
  vpc_id = aws_vpc.shopping_vpc.id

  tags = {
    Name = "shopping-internet-gateway"
  }
}
