//public route table
resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.shopping_vpc.id //	Identifies the VPC where this route table belongs
  tags = {
    Name = "shopping-public-route-table"
  }
}

//Public route with internet gateway
resource "aws_route" "public_internet_route" {
  route_table_id         = aws_route_table.public_route_table.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.shopping_igw.id
}

//pubic subnet connected with the public route table
resource "aws_route_table_association" "public_subnet_association" {
  route_table_id = aws_route_table.public_route_table.id
  subnet_id      = aws_subnet.public_subnet.id
}

//private route table
resource "aws_route_table" "private_route_table" {
  vpc_id = aws_vpc.shopping_vpc.id
  tags = {
    Name = "shopping-private-route-table"
  }
}

//private route table route and the way to go outside not using igw
# resource "aws_route" "private_nat_route" {
#   route_table_id = aws_route_table.private_route_table.id
#   //It's called a default route.
#   destination_cidr_block = "0.0.0.0/0"
#   nat_gateway_id         = aws_nat_gateway.shopping_nat.id
# }

//connecting private subnet with the private route table
resource "aws_route_table_association" "private_subnet_association" {
  route_table_id = aws_route_table.private_route_table.id
  subnet_id      = aws_subnet.private_subnet.id
}