resource "aws_subnet" "public_subnet" {
  vpc_id                  = aws_vpc.shopping_vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "ap-south-1a"
  map_public_ip_on_launch = true

  tags = {
    Name = "shopping-public-subnet"
  }
}
//Important: A private subnet is determined by its routing, not just this setting.
// We will ensure it has no direct route to an Internet Gateway.
resource "aws_subnet" "private_subnet" {
  vpc_id                  = aws_vpc.shopping_vpc.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "ap-south-1a"
  map_public_ip_on_launch = false //We don't want instances launched in this subnet to automatically receive public IPv4 addresses.

  tags = {
    Name = "shopping-private-subnet"
  }
}

//public subnet 2. No need to define separate route as we are using existing route table so same route we can use.
resource "aws_subnet" "public_subnet_2" {
  vpc_id                  = aws_vpc.shopping_vpc.id
  cidr_block              = "10.0.3.0/24"
  availability_zone       = "ap-south-1b"
  map_public_ip_on_launch = true

  tags = {
    Name = "shopping-public-subnet-2"
  }
}

//association public subnet with public route table
resource "aws_route_table_association" "public_subnet_2_associate" {
  route_table_id = aws_route_table.public_route_table.id
  subnet_id      = aws_subnet.public_subnet_2.id
}
