cmake_minimum_required(VERSION 3.20)
project(untitled3)

set(CMAKE_CXX_STANDARD 14)

add_executable(untitled3 Boost_Echo_Client/src/main.cpp Boost_Echo_Client/src/keyboardThreadTask.cpp Boost_Echo_Client/include/keyboardThreadTask.h Boost_Echo_Client/src/socketTreadTask.cpp Boost_Echo_Client/include/socketTreadTask.h)


set(Boost_INCLUDE_DIR /cygdrive/c/cygwin64/usr/include)

find_package(Boost REQUIRED COMPONENTS system)
include_directories(${Boost_INCLUDE_DIRS})

target_link_libraries(${PROJECT_NAME} ${Boost_LIBRARIES})