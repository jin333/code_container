def Radix (list,i):
	temp_list = [[],[],[],[],[],[],[],[],[],[]]
	temp_need = []
	temp_not = []
	
	if i != 0 :
		for k in range(len(list)) :
			if list[k]//(10*i) : 
				temp_need.append(list[k])
			else :
				temp_not.append(list[k])
	else :
		for k in range(len(list)) :
			temp_list[list[k]%10].append(list[k])
		
	for k in range(len(temp_need)) :
		temp_list[(temp_need[k]//(10*i))%10].append(temp_need[k])
	
	#print(temp_list)
	
	for i in range(len(temp_list)) :
		for j in range(len(temp_list[i])) :
			temp_not.append(temp_list[i].pop(0))
			
	return temp_not

def checkMax(list):
	Max = 0
	for k in range(len(list)):
		if list[k] > Max :
			Max = list[k]
	
	Max_ = 1
	if Max < 10 :
		return 0
		
	while True :
		if Max//(10*Max_) != 0 :
			Max_ += 1
		else :
			return Max_

def do (list):
	size = checkMax(list)

	for k in range(size):
		list = Radix(list,k)

	return list
	
temp = list(map(int,input(Enter the value : ).split()))

print(do(temp))